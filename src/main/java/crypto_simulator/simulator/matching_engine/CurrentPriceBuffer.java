package crypto_simulator.simulator.matching_engine;

public interface CurrentPriceBuffer {
    double getBestBidPrice();
    void setBestBidPrice(double price);
    double getBestAskPrice();
    void setBestAskPrice(double price);
}
