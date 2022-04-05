package crypto_simulator.simulator.router;

public interface RouterPriceInfo {
    public void send(double price) throws InterruptedException;

    public double receive() throws InterruptedException;

}