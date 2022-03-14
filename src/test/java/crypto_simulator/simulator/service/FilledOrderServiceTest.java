package crypto_simulator.simulator.service;

import crypto_simulator.simulator.domain.Order;
import crypto_simulator.simulator.domain.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FilledOrderServiceTest {
    @Autowired PositionService positionService;
    @Autowired MemberService memberService;
    @Autowired FilledOrderService filledOrderService;

    @Test
    @Rollback(value = false)
    public void initAndFindMemberIdTicker() {
        //given
        Member member = new Member("user1", "1234", 10000, 0);

        Long savedId = memberService.join(member);
        positionService.initiatePosition(member); // finished registration

        Order order = new Order(Ticker.BTCUSD, OrderStatus.OPEN, OrderType.BUY,
                40000, 10, 0.1, 20, 90000, 0, 1L);

        Order newSellOrder = new Order( Ticker.BTCUSD, OrderStatus.OPEN, OrderType.SELL,
                50000, 5, 0.1, 20, 0, 250000,1L);

        System.out.println("check ID");
        System.out.println(order.getId() );
        System.out.println(newSellOrder.getId());

        filledOrderService.saveFilledOrder(order, member);
        filledOrderService.saveFilledOrder(newSellOrder, member);

        member.updateUsdBalanceOpen(order);
        member.updateUsdBalanceFilled(order);

        Position position = positionService.findByMemberIdTicker(member, Ticker.BTCUSD);
        position.updatePositionOpen(order);
        position.updatePositionFilled(order);
        position.updatePositionOpen(newSellOrder);
        position.updatePositionFilled(newSellOrder);

        //when
        List<FilledOrders> filledOrdersList = filledOrderService.findByMember(member);

        //then
        Assertions.assertEquals(filledOrdersList.size(), 2);
    }
}