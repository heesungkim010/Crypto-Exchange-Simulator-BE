package crypto_simulator.simulator;

import crypto_simulator.simulator.matching_engine.*;
import crypto_simulator.simulator.router.Router;
import crypto_simulator.simulator.router.RouterOneToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Setter @Getter
public class AppConfig {
    //setting info. tickers.
    private ArrayList<String> tickerArray = new ArrayList<String>();
    private List<MatchingEngineBUY> matchingEngineBUYList = new ArrayList<MatchingEngineBUY>();

    private int apiMatchingEngineBufferSize = 10000;
    private String[] startingTickersArray = {"btc"};
    private double[][] indexPriceList = { {10000, 100000, 0.5, 180000}, {1000, 10000, 0.05, 180000 }};
    private Map<String, Router> apiMeRouterHashMap;

    /*
    private double startIndexPrice;
    private double endIndexPrice;
    private double indexGapPrice;
    private int lengthOfList
    */
    // TODO : some other way to initiate indexPriceList

    public AppConfig() throws InterruptedException {
        this.apiMeRouterHashMap = new ConcurrentHashMap<>();
        for (String ticker: startingTickersArray) {
            putTickerArray(ticker);
            this.apiMeRouterHashMap.put(
                    ticker, new RouterOneToOne(this.apiMatchingEngineBufferSize));
        }

        int index = 0;
        for (String ticker : tickerArray){
            CurrentPriceBuffer priceBuffer = initCurrentPriceBuffer(ticker);
            ExternalPriceInfoReceiver priceInfoReceiver = initExternalPriceInfoReceiver(ticker, priceBuffer);

            MatchingEngineBUY matchingEngineBUY = new MatchingEngineBUY(ticker, indexPriceList[index++],
                    this.apiMeRouterHashMap.get(ticker), priceBuffer, priceInfoReceiver);

            ReqConsumerMe reqConsumerMeBUY = initOrderConsumerMeBUY(ticker,
                    matchingEngineBUY, this.apiMeRouterHashMap.get(ticker));

            Thread threadReqConsume = new Thread(reqConsumerMeBUY, "reqConsumerMeBUY");
            threadReqConsume.start();
/*
        // new CurrentPriceBufferImpl(ticker); -- ticker buy/sell --> one per ticker
        // new ExternalPriceInfoReceiverImpl(ticker, this.currentPriceBuffer); ticker buy/sell --> one per ticker
        //OrderConsumerMe ;  ticker buy one  ticker sell one. --. two per ticker
        //(String ticker, MatchingEngineBUY me, Router apiMeRouter)
 */
        }
    }
    public CurrentPriceBuffer initCurrentPriceBuffer(String ticker){
        return new CurrentPriceBufferImpl(ticker);
    }

    public ExternalPriceInfoReceiver initExternalPriceInfoReceiver(
            String ticker, CurrentPriceBuffer buffer){
        return new ExternalPriceInfoReceiverImpl(ticker, buffer);
    }

    public ReqConsumerMe initOrderConsumerMeBUY(
            String ticker, MatchingEngineBUY me, Router router){
        return new ReqConsumerMeBUYImpl(ticker, me, router);
    }

    public void putTickerArray(String ticker){

        this.tickerArray.add(ticker);
    }
}
