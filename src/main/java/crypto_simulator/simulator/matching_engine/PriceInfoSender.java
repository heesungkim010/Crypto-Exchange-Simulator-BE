package crypto_simulator.simulator.matching_engine;

public interface PriceInfoSender {
    double getCurBestBidPrice();
    void setCurBestBidPrice(double price);

    double getCurBestAskPrice();
    void setCurBestAskPrice(double price);
}