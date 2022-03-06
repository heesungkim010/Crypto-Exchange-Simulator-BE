package crypto_simulator.simulator.matching_engine;

import lombok.RequiredArgsConstructor;

public class MatchingEngine {

    private String ticker;
    private ExternalPriceInfoReceiver priceInfoReceiver;
    private CurrentPriceBuffer currentPriceBuffer;

    public MatchingEngine(String ticker) throws InterruptedException {
        this.ticker = ticker;
        this.currentPriceBuffer = new CurrentPriceBuffer(ticker);
        this.priceInfoReceiver = new ExternalPriceInfoReceiver(ticker, this.currentPriceBuffer);
    }

    // TODO : init and re-init every 12 hours(because of binance websocket limitation rules)
    //       re-init when websocket error

    // currentPriceBuffer.getBestBidPrice()
    // currentPriceBuffer.getBestAskPrice()


}
