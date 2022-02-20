package crypto_simulator.simulator.repository;

import crypto_simulator.simulator.domain.Position;
import crypto_simulator.simulator.domain.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class PositionRepository {

    private final EntityManager em;

    public void saveInitSignUp(Position position){
        em.persist(position);
    }

    public void saveNewTicker(Position position){
        em.persist(position);
    }

    public Position findByMemberIdTicker(Long memberId, Ticker ticker){
        return em.createQuery("select cp from Position cp where cp.id = :memberId and cp.ticker = :ticker", Position.class)
                .setParameter("memberId", memberId)
                .setParameter("ticker", ticker)
                .getSingleResult();
    }
}