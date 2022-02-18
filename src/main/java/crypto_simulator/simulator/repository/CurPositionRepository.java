package crypto_simulator.simulator.repository;

import crypto_simulator.simulator.domain.CurPosition;
import crypto_simulator.simulator.domain.Member;
import crypto_simulator.simulator.domain.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class CurPositionRepository {

    private final EntityManager em;

    public void saveInitSignUp(CurPosition curPosition){
        em.persist(curPosition);
    }

    public void saveNewTicker(CurPosition curPosition){
        em.persist(curPosition);
    }

    public CurPosition findByMemberIdTicker(Long memberId, Ticker ticker){
        return em.createQuery("select cp from CurPosition cp where cp.id = :memberId and cp.ticker = :ticker", CurPosition.class)
                .setParameter("memberId", memberId)
                .setParameter("ticker", ticker)
                .getSingleResult();
    }
}
