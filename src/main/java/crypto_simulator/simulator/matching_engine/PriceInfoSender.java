package crypto_simulator.simulator.matching_engine;

public interface PriceInfoSender extends Runnable{
    double getCurBestBidPrice();
    void setCurBestBidPrice(double price);

    double getCurBestAskPrice();
    void setCurBestAskPrice(double price);

    void checkPriceAndSendPrice() throws InterruptedException;
}