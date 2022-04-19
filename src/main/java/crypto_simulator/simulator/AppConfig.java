package crypto_simulator.simulator;

import crypto_simulator.simulator.data_center.DataCenter;
import crypto_simulator.simulator.matching_engine.*;
import crypto_simulator.simulator.router.*;
import crypto_simulator.simulator.service.FilledOrderService;
import crypto_simulator.simulator.service.MemberService;
import crypto_simulator.simulator.service.PositionService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    private int meBuyPriceSenderBufferSize = 10000;
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
        // {ticker : router(api-MeBUY) }
        for (String ticker: startingTickersArray) {
            putTickerArray(ticker);
            this.apiMeBUYRouterHashMap.put(
                    ticker, new RouterManyToOne(this.apiMatchingEngineBufferSize));
        }

        /*
        Initialize receivers, senders, routers, matching engines, data centers
        with interface : ExternalPriceInfoReceiver/ PriceInfoSender / MeToDataCenterRouter
        without interface : matching engine, data center
        */
        int index = 0;
        for (String ticker : tickerArray){
            ExternalPriceInfoReceiver externalPriceInfoReceiver = initExternalPriceInfoReceiver(ticker);
            Router meToDcRouter = initMeToDataCenterRouter(this.meDataCenterBufferSize);
            RouterPriceInfo meBuyToPriceSenderRouter = initMeBuyToPriceSenderRouter(this.meBuyPriceSenderBufferSize);
            PriceInfoSender priceInfoSender= initPriceInfoSender(ticker, meBuyToPriceSenderRouter);

            MatchingEngineBUY matchingEngineBUY = initMatchingEngineBUY(ticker, indexPriceList[index++],
                    externalPriceInfoReceiver,
                    meToDcRouter, meBuyToPriceSenderRouter); //matchingEngineBUY has meToPriceSenderRouter

            DataCenter dataCenter = new DataCenter(
                    ac.getBean(MemberService.class),
                    ac.getBean(FilledOrderService.class),
                    ac.getBean(PositionService.class),
                    meToDcRouter, ticker);
            //non init-functions end.

            ReqConsumerMe reqConsumerMeBUY = initOrderConsumerMeBUY(ticker,
                    matchingEngineBUY, this.apiMeBUYRouterHashMap.get(ticker));
            //finished dependency injection.

            Thread threadReqConsumer = new Thread(reqConsumerMeBUY, "reqConsumerMeBUY");
            Thread threadUpdateAndFill = new Thread(matchingEngineBUY, "updateAndFill");
            Thread threadCheckPriceAndSendPrice = new Thread(priceInfoSender, "checkPriceAndSendPrice");
            Thread threadFillOrders = new Thread(dataCenter, "fillOrders");

            threadReqConsumer.start();
            threadUpdateAndFill.start();
            threadCheckPriceAndSendPrice.start();
            threadFillOrders.start();
        /*
        Result
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
            String ticker, RouterPriceInfo meBuyToPriceSenderRouter){
        return new PriceInfoSenderImpl(ticker, meBuyToPriceSenderRouter);
    }

    public Router initMeToDataCenterRouter(int buffersize){
        return new RouterManyToOne(buffersize);
    }
    public RouterPriceInfo initMeBuyToPriceSenderRouter(int buffersize){
        return new RouterOneToOnePriceInfo(buffersize);
    }
    public MatchingEngineBUY initMatchingEngineBUY(String ticker, double[] indexPriceList,
                                       ExternalPriceInfoReceiver priceInfoReceiver,
                                       Router meToDataCenterRouter, RouterPriceInfo meBuyToPriceSenderRouter) throws InterruptedException {
        return new MatchingEngineBUY(ticker, indexPriceList, priceInfoReceiver,
                meToDataCenterRouter, meBuyToPriceSenderRouter);
    }

    public void putTickerArray(String ticker){
        this.tickerArray.add(ticker);
    }
}
