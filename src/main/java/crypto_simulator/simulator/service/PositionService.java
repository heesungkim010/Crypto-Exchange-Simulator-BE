package crypto_simulator.simulator.service;

import crypto_simulator.simulator.domain.Member;
import crypto_simulator.simulator.domain.Order;
import crypto_simulator.simulator.domain.Position;
import crypto_simulator.simulator.domain.Ticker;
import crypto_simulator.simulator.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static crypto_simulator.simulator.domain.Position.createPosition;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;

    @Transactional
    public void initiatePosition(Member member){
        //initiate Position when first registered.
        // 1. create Positions of all tickers : domain Position
        // 2. save all the Positions : repository PositionRepository
        Ticker tickers[] = Ticker.values();
        for(Ticker ticker : tickers){
            Position position = createPosition(member, ticker);
            positionRepository.saveInitSignUp(position);
        }
        //TODO : check if (1) using cascade or
        // (2) using Position[] and pass to repository would be better
    }

    @Transactional
    public void addNewPosition(Member member){
        //add new Position when new ticker added
        // 1. create Positions of the new tickers : domain Position
        // 2. save the Positions : repository PositionRepository
        //TODO : how to deal with the running server when new tickers added?
    }

    @Transactional
    public void updatePositionOpen(Member member, Order order){
        Position position = findByMemberIdTicker(member, order.getTicker());
        position.updatePositionOpen(order);
    }

    @Transactional
    public void updatePositionFilled(Member member, Order order){
        Position position = findByMemberIdTicker(member, order.getTicker());
        position.updatePositionFilled(order);
    }

    @Transactional
    public void updatePositionCanceled(Member member, Order order){
        Position position = findByMemberIdTicker(member, order.getTicker());
        position.updatePositionCanceled(order);
    }

    public Position findByMemberIdTicker(Member member, Ticker ticker){
        return positionRepository.findByMemberIdTicker(member, ticker);
    }
}