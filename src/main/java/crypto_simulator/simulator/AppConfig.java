package crypto_simulator.simulator;

import crypto_simulator.simulator.matching_engine.MatchingEngine;
import org.thymeleaf.standard.expression.Each;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppConfig {
    //setting info. tickers.
    private String[] tickerArray = {"btc", "eth"};




    private List<String> tickerList = new ArrayList<String>();

    public AppConfig() {
        for (String ticker: tickerArray) {
            tickerList.add(ticker);
        }
        tickerList.stream()
                .map( m-> new MatchingEngine(m))
                .collect(Collectors.toList());
    }
}
