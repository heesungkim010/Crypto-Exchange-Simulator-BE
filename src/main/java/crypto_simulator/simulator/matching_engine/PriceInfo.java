package crypto_simulator.simulator.matching_engine;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PriceInfo {
    private String u;
    private String s;
    private String b;
    private String B;
    private String a;
    private String A;
    //https://binance-docs.github.io/apidocs/spot/en/#all-market-tickers-stream

    /*
    {"u":17587026589,"s":"BTCUSDT","b":"39465.90000000","B":"3.87954000","a":"39465.91000000","A":"1.53565000"}

     */
}