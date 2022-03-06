package crypto_simulator.simulator;

import crypto_simulator.simulator.matching_engine.MatchingEngine;
import org.thymeleaf.standard.expression.Each;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppConfig {
    //setting info. tickers.
    private String[] tickerArray = {"btc","eth"};
    List<MatchingEngine> matchingEngineList = new ArrayList<MatchingEngine>();


    public AppConfig() throws InterruptedException {


        for (String ticker : tickerArray){
            matchingEngineList.add(new MatchingEngine(ticker));
        }
        System.out.println("app config ends");

        for (MatchingEngine me: matchingEngineList ) {
            me.testBuffer();
        }
    }

}
