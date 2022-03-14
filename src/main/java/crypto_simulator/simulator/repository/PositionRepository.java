package crypto_simulator.simulator.repository;

import crypto_simulator.simulator.domain.Member;
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

    public Position findByMemberIdTicker(Member member, Ticker ticker){
        System.out.println("position repository");
        return em.createQuery("select p from Position p where p.memberInPosition = :member and p.ticker = :ticker", Position.class)
                .setParameter("member", member)
                .setParameter("ticker", ticker)
                .getSingleResult();

    }
}