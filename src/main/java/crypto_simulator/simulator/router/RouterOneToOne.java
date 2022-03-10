package crypto_simulator.simulator.router;

import crypto_simulator.simulator.domain.Order;

import java.util.concurrent.Semaphore;

public class RouterOneToOne implements Router{
    private int in, out, bufferSize;
    private Semaphore  nrfull, nrempty;
    private Order[] buffer;

    public RouterOneToOne(int bufferSize){
        this.in = 0;
        this.out = 0;
        this.bufferSize = bufferSize;
        this.nrfull = new Semaphore(0, true);
        this.nrempty = new Semaphore(bufferSize, true);
        this.buffer = new Order[bufferSize];
    }

    @Override
    public void send(Order order) throws InterruptedException {
        nrempty.acquire();

        buffer[in] = order;
        in = (in+1) % this.bufferSize;

        nrfull.release();
    }

    @Override
    public Order receive() throws InterruptedException {
        nrfull.acquire();

        Order output = buffer[out];
        out = (out+1) % this.bufferSize;

        nrempty.release();
        return output;
    }

}
