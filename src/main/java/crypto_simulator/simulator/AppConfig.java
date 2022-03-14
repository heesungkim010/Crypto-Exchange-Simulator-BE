package crypto_simulator.simulator;

import crypto_simulator.simulator.data_center.DataCenter;
import crypto_simulator.simulator.matching_engine.*;
import crypto_simulator.simulator.router.Router;
import crypto_simulator.simulator.router.RouterManyToOne;
import crypto_simulator.simulator.service.FilledOrderService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
    private int meDataCenterBufferSize = 10000;
    private String[] startingTickersArray = {"btc"};
    private double[][] indexPriceList = { {10000, 100000, 0.5, 180000}, {1000, 10000, 0.05, 180000 }};
    private Map<String, Router> apiMeBUYRouterHashMap;
    private Map<String, PriceInfoSender> priceInfoSenderHashMap;
    private ApplicationContext ac;
    /*
    private double startIndexPrice;
    private double endIndexPrice;
    private double indexGapPrice;
    private int lengthOfList
    */
    // TODO : some other way to initiate indexPriceList

    /*
    below : init functions + non- init functons
     */

    @Autowired
    public AppConfig(ApplicationContext applicationContext) throws InterruptedException {
        this.ac = applicationContext;
        start();
    }

    public void start() throws InterruptedException {

        this.apiMeBUYRouterHashMap = new ConcurrentHashMap<>();
        //non-init function
        for (String ticker: startingTickersArray) {
            putTickerArray(ticker);
            this.apiMeBUYRouterHashMap.put(
                    ticker, new RouterManyToOne(this.apiMatchingEngineBufferSize));
            //non-init function
        }

        int index = 0;
        for (String ticker : tickerArray){
            ExternalPriceInfoReceiver externalPriceInfoReceiver = initExternalPriceInfoReceiver(ticker);
            PriceInfoSender priceInfoSender= initPriceInfoSender(ticker);
            Router meToDcRouter = initMeToDataCenterRouter(this.meDataCenterBufferSize);

            // Init Matching Engine and DataCenter -- > without interface
            // with interface : ExternalPriceInfoReceiver/ PriceInfoSender / MeToDataCenterRouter
            MatchingEngineBUY matchingEngineBUY = initMatchingEngineBUY(ticker, indexPriceList[index++],
                    externalPriceInfoReceiver,
                    priceInfoSender,
                    meToDcRouter);

            DataCenter dataCenter = new DataCenter(ac.getBean(FilledOrderService.class),
                    meToDcRouter, ticker); // ac.getBean
            //non init-functions end.

            ReqConsumerMe reqConsumerMeBUY = initOrderConsumerMeBUY(ticker,
                    matchingEngineBUY, this.apiMeBUYRouterHashMap.get(ticker));

            Thread threadReqConsume = new Thread(reqConsumerMeBUY, "reqConsumerMeBUY");
            threadReqConsume.start();
        /*
        new CurrentPriceBufferImpl(ticker); -- ticker buy/sell --> one per ticker
        new ExternalPriceInfoReceiverImpl(ticker, this.currentPriceBuffer); ticker buy/sell --> one per ticker
        OrderConsumerMe;  ticker buy one  ticker sell one. --. two per ticker
        (String ticker, MatchingEngineBUY me, Router apiMeRouter)
         */
        }
    }

    public ExternalPriceInfoReceiver initExternalPriceInfoReceiver(
            String ticker){
        return new ExternalPriceInfoReceiverImpl(ticker);
    }

    public ReqConsumerMe initOrderConsumerMeBUY(
            String ticker, MatchingEngineBUY me, Router router){
        return new ReqConsumerMeBUYImpl(ticker, me, router);
    }

    public PriceInfoSender initPriceInfoSender(
            String ticker){

        return new PriceInfoSenderImpl(ticker);
    }

    public Router initMeToDataCenterRouter(int buffersize){
        return new RouterManyToOne(buffersize);
    }

    public MatchingEngineBUY initMatchingEngineBUY(String ticker, double[] indexPriceList,
                                       ExternalPriceInfoReceiver priceInfoReceiver,
                                       PriceInfoSender priceInfoSender,
                                       Router meToDataCenterRouter) throws InterruptedException {
        return new MatchingEngineBUY(ticker, indexPriceList, priceInfoReceiver,
                priceInfoSender, meToDataCenterRouter);
    }



    public void putTickerArray(String ticker){
        this.tickerArray.add(ticker);
    }
}
