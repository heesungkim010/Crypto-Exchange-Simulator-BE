package crypto_simulator.simulator.router;

import java.util.concurrent.Semaphore;

public class RouterOneToOnePriceInfo implements RouterPriceInfo{
    private int in, out, bufferSize;
    private Semaphore  nrfull, nrempty;
    private double[] buffer;

    public RouterOneToOnePriceInfo(int bufferSize){
        this.in = 0;
        this.out = 0;
        this.bufferSize = bufferSize;
        this.nrfull = new Semaphore(0, true);
        this.nrempty = new Semaphore(bufferSize, true);
        this.buffer = new double[bufferSize];
    }

    @Override
    public void send(double price) throws InterruptedException {
        nrempty.acquire();

        buffer[in] = price;
        in = (in+1) % this.bufferSize;

        nrfull.release();
    }

    @Override
    public double receive() throws InterruptedException {
        nrfull.acquire();

        double output = buffer[out];
        out = (out+1) % this.bufferSize;

        nrempty.release();
        return output;
    }

}
