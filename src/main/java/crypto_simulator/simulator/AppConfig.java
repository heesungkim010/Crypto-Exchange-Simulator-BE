package crypto_simulator.simulator;

import crypto_simulator.simulator.domain.Order;
import crypto_simulator.simulator.matching_engine.MatchingEngine;
import crypto_simulator.simulator.router.Router;
import crypto_simulator.simulator.router.RouterOneToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.thymeleaf.standard.expression.Each;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
@Setter @Getter
public class AppConfig {
    //setting info. tickers.
    private ArrayList<String> tickerArray = new ArrayList<String>();
    private List<MatchingEngine> matchingEngineList = new ArrayList<MatchingEngine>();

    private int apiMatchingEngineBufferSize = 10000;
    private String[] startingTickersArray = {"btc", "eth"};
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
            matchingEngineList.add(new MatchingEngine(
                    ticker, indexPriceList[index++], this.apiMeRouterHashMap.get(ticker)));
        }
    }

    public void putTickerArray(String ticker){
        this.tickerArray.add(ticker);
    }
}
