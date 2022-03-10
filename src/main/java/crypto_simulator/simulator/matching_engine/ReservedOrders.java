package crypto_simulator.simulator.matching_engine;

import crypto_simulator.simulator.domain.Order;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ReservedOrders {
    private Map<Long, Order> hashMap;

    public ReservedOrders() {
        this.hashMap = new HashMap<>();
        //lock, unlock not needed here. already done before
    }

    public void addOrder(Order order) {
        this.hashMap.put(order.getId(), order);
    }

}
