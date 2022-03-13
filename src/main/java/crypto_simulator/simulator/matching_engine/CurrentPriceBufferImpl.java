package crypto_simulator.simulator.matching_engine;

import lombok.Getter;
import lombok.Setter;

public class CurrentPriceBufferImpl implements CurrentPriceBuffer{
    private String ticker;
    private double bestBidPrice;
    private double bestAskPrice;

    public CurrentPriceBufferImpl(String ticker) {
        this.ticker = ticker;
    }

    public double getBestBidPrice() {
        return bestBidPrice;
    }

    public void setBestBidPrice(double bestBidPrice) {
        this.bestBidPrice = bestBidPrice;
    }

    public double getBestAskPrice() {
        return bestAskPrice;
    }

    public void setBestAskPrice(double bestAskPrice) {
        this.bestAskPrice = bestAskPrice;
    }
}