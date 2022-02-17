package crypto_simulator.simulator.repository;

import crypto_simulator.simulator.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member){
        //System.out.println(member.getId());
        em.persist(member);
        //System.out.println(member.getId());
    }


    public Member findById(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findByUserId(String userId){
        return em.createQuery("select m from Member m where m.userId = :userId", Member.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
