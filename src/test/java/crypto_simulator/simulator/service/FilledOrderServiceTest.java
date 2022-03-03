package crypto_simulator.simulator.service;

import crypto_simulator.simulator.NewOrder;
import crypto_simulator.simulator.domain.*;
import crypto_simulator.simulator.repository.PositionRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;


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
        Member member = new Member();
        member.setUserId("user1");
        member.setUsuableMoney(100000);
        member.setCurOrderedMoney(0);
        Long savedId = memberService.join(member);
        positionService.initiatePosition(member); // finished registration

        NewOrder newOrder = new NewOrder(1L, Ticker.BTCUSD, OrderStatus.OPEN, OrderType.BUY,
                40000, 10, 0.1, 20, 90000, 0,LocalDateTime.now(),1L);

        filledOrderService.saveFilledOrder(newOrder, member);

        member.updateUsdBalanceOpen(newOrder);
        member.updateUsdBalanceFilled(newOrder);

        Position position = positionService.findByMemberIdTicker(savedId, Ticker.BTCUSD);
        position.updatePositionOpen(newOrder);
        position.updatePositionFilled(newOrder);

        //when
        List<FilledOrders> filledOrdersList = filledOrderService.findByMember(member);

        //then
        Assertions.assertEquals(filledOrdersList.size(), 1);
    }

}