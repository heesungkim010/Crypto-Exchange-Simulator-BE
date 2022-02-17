package crypto_simulator.simulator.service;

import crypto_simulator.simulator.domain.Member;
import crypto_simulator.simulator.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void register() {
        //given
        Member member = new Member();
        member.setUserId("user1");

        //when
        Long savedId = memberService.join(member);

        //then
        Assertions.assertEquals(member, memberRepository.findByUserId("user1"));
    }

    @Test(expected = IllegalStateException.class)
    public void DupRegister() {
        //given
        Member member1 = new Member();
        member1.setUserId("user1");
        Member member2 = new Member();
        member2.setUserId("user1");

        //when
        Long savedId = memberService.join(member1);
        Long savedId2 = memberService.join(member2);

        //then
        Assertions.fail("needs to fail");
    }
}