package crypto_simulator.simulator.matching_engine;

import crypto_simulator.simulator.domain.Orders;

import java.util.HashMap;
import java.util.Map;

public class ReservedOrders {
    private Map<Long, Orders> hashMap;

    public ReservedOrders() {
        this.hashMap = new HashMap<>();
        //lock, unlock not needed here. already done before
    }
}
