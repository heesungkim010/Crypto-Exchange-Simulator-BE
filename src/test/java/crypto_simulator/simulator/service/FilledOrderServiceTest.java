package crypto_simulator.simulator.service;

import crypto_simulator.simulator.domain.FilledOrders;
import crypto_simulator.simulator.domain.Member;
import crypto_simulator.simulator.domain.Position;
import crypto_simulator.simulator.domain.Ticker;
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
        Long savedId = memberService.join(member);
        positionService.initiatePosition(member);

        filledOrderService.saveFilledOrder(member);

        //when
        List<FilledOrders> filledOrdersList = filledOrderService.findByMember(member);

        //then
        Assertions.assertEquals(filledOrdersList.size(), 1);
    }

}