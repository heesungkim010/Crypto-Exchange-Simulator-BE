package crypto_simulator.simulator.service;

import crypto_simulator.simulator.domain.Member;
import crypto_simulator.simulator.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member){
        checkDuplicate(member);
        memberRepository.save(member);
        return member.getId();
    }

    public Member findById(Long id){
        return memberRepository.findById(id);
    }

    public Member findByUserId(String UserId){
        return memberRepository.findByUserId(UserId);
    }

    private void checkDuplicate(Member member) {
        List<Member> memberList = memberRepository.findListByUserId(member.getUserId());
        if(!memberList.isEmpty()){
            throw new IllegalStateException("Already existing userId");
        }
    }
}
