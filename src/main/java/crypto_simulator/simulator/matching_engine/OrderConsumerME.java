package crypto_simulator.simulator.matching_engine;

public class OrderConsumerME {
    /*
    gets order from receiver
    checks order type
    call methods in matchingEngine accordingly.
     */
    private String ticker;
    private MatchingEngine matchingEngine;

    public OrderConsumerME(String ticker, MatchingEngine me) {
        this.ticker = ticker;
        this.matchingEngine = me;
    }
}
