package crypto_simulator.simulator.service;

import crypto_simulator.simulator.domain.Member;
import crypto_simulator.simulator.domain.Position;
import crypto_simulator.simulator.domain.Ticker;
import crypto_simulator.simulator.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PositionServiceTest {

    @Autowired PositionService positionService;
    @Autowired MemberService memberService;

    @Test
    @Rollback(value = false)
    public void initAndFindMemberIdTicker() {
        //given
        Member member = new Member();
        member.setUserId("user1");

        //when
        Long savedId = memberService.join(member);
        positionService.initiatePosition(member);
        Position found_position = positionService.findByMemberIdTicker(savedId, Ticker.BTCUSD);

        //then
        Assertions.assertEquals(found_position.getMemberInPosition(), member);
    }
}