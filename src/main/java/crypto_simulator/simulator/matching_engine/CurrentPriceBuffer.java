package crypto_simulator.simulator.matching_engine;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CurrentPriceBuffer {
    private String ticker;
    private double curPrice;

    public CurrentPriceBuffer(String ticker) {
        this.ticker = ticker;
    }
}