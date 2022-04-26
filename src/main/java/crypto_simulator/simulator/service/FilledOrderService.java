package crypto_simulator.simulator.service;

import crypto_simulator.simulator.domain.Order;
import crypto_simulator.simulator.domain.*;
import crypto_simulator.simulator.repository.FilledOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static crypto_simulator.simulator.domain.FilledOrder.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FilledOrderService {
    private final FilledOrderRepository filledOrderRepository;

    @Transactional
    public void saveFilledOrder(Order order, Member member){
        // save filled order when filled.
        // 1. create FilledOrder object : domain FilledOrder
        // 2. save the object. : respository FilledOrderRepository
        FilledOrder filledOrder = createFilledOrder(order, member);
        filledOrder.setMemberInFilledOrder(member);
        filledOrderRepository.saveFilledOrder(filledOrder);
        //TODO : check if (1) using cascade or
        // (2) using Position[] and pass to repository would be better
    }

    public List<FilledOrder> findByMember(Member member){
        return filledOrderRepository.findByMember(member);
    }
}
