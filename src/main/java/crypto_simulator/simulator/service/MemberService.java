package crypto_simulator.simulator.service;

import crypto_simulator.simulator.domain.Member;
import crypto_simulator.simulator.domain.Order;
import crypto_simulator.simulator.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    @Transactional
    public Long join(Member member) throws NoSuchAlgorithmException {
        checkDuplicate(member);
        //SHA algo for password
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(
                (member.getPassword()+"!gkRg*4Gp*4gaqz&TmapIfQgd").getBytes(StandardCharsets.UTF_8));
        member.setPassword(bytesToHex(hashbytes));

        memberRepository.save(member);
        return member.getId();
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
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

    @Transactional
    public void updateUsdBalanceOpen(Long memberId, Order order){
        Member member = findById(memberId);
        member.updateUsdBalanceOpen(order);
    }

    @Transactional
    public void updateUsdBalanceFilled(Long memberId, Order order){
        Member member = findById(memberId);
        member.updateUsdBalanceFilled(order);
    }

    @Transactional
    public void updateUsdBalanceCanceled(Long memberId, Order order){
        Member member = findById(memberId);
        member.updateUsdBalanceCanceled(order);
    }

}
