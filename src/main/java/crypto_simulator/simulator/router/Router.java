package crypto_simulator.simulator.router;

import crypto_simulator.simulator.domain.Order;

public interface Router {
    public void send(Order o) throws InterruptedException;

    public Order receive() throws InterruptedException;

}