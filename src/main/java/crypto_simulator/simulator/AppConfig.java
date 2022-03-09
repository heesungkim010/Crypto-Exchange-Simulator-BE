package crypto_simulator.simulator;

import crypto_simulator.simulator.domain.Order;
import crypto_simulator.simulator.matching_engine.MatchingEngine;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.thymeleaf.standard.expression.Each;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Setter
public class AppConfig {
    //setting info. tickers.
    ArrayList<String> tickerArray = new ArrayList<String>();
    List<MatchingEngine> matchingEngineList = new ArrayList<MatchingEngine>();

    private String[] startingTickersArray = {"btc", "eth"};
    private double[][] indexPriceList = { {10000, 100000, 0.5, 180000}, {1000, 10000, 0.05, 180000 }};

    /*
    private double startIndexPrice;
    private double endIndexPrice;
    private double indexGapPrice;
    private int lengthOfList
    */
    // TODO : some other way to initiate indexPriceList

    public AppConfig() throws InterruptedException {

        for (String ticker: startingTickersArray) {
            putTickerArray(ticker);
        }

        // initiating MatchingEngines
        int index = 0;
        for (String ticker : tickerArray){
            matchingEngineList.add(new MatchingEngine(ticker, indexPriceList[index++]));
        }
    }

    public void putTickerArray(String ticker){
        this.tickerArray.add(ticker);
    }
}
