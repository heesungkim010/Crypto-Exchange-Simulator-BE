package crypto_simulator.simulator.repository;

import crypto_simulator.simulator.domain.FilledOrders;
import crypto_simulator.simulator.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilledOrderRepository {
    private final EntityManager em;

    public void saveFilledOrder(FilledOrders filledOrder){
        em.persist(filledOrder);
    }

    public List<FilledOrders> findByMember(Member member){
        //TODO : LAZY LOADING
        return em.createQuery("select fo from FilledOrders fo where fo.memberInFilledOrders = :member", FilledOrders.class)
                .setParameter("member", member)
                .getResultList();
    }
}
