package crypto_simulator.simulator.matching_engine;

import crypto_simulator.simulator.domain.Order;
import crypto_simulator.simulator.router.Router;

import java.util.concurrent.Semaphore;

public class MatchingEngine {

    private String ticker;
    private ExternalPriceInfoReceiver priceInfoReceiver;
    private CurrentPriceBuffer currentPriceBuffer;
    private OrderConsumerME receivingBufferME;
    private double curBestBidPrice;
    private double prevBestBidPrice;
    private double curBestAskPrice;
    private double prevBestAskPrice;
    private Router apiMeRouter;

    private Semaphore mutex;
    private ReservedOrders[] priceIndexArray;
    private double startIndexPrice;
    private double endIndexPrice;
    private double indexGapPrice;

    public MatchingEngine(String ticker, double[] indexPriceList, Router apiMeRouter) throws InterruptedException {
        this.ticker = ticker;
        this.currentPriceBuffer = new CurrentPriceBuffer(ticker);
        this.priceInfoReceiver = new ExternalPriceInfoReceiver(ticker, this.currentPriceBuffer);

        this.mutex = new Semaphore(1, true);
        this.startIndexPrice = indexPriceList[0];
        this.endIndexPrice = indexPriceList[1];
        this.indexGapPrice = indexPriceList[2];
        this.priceIndexArray = new ReservedOrders[(int) indexPriceList[3]];

        this.receivingBufferME = new OrderConsumerME(ticker, this);
    }

    // TODO : init and re-init every 12 hours(because of binance websocket limitation rules)
    //       re-init when websocket error

    // currentPriceBuffer.getBestBidPrice()
    // currentPriceBuffer.getBestAskPrice()

    public void openOrder(Order order) throws InterruptedException {
        /*
        lock
        check bestBidPrice;
        do market filled or reserve order at limit price
        unlock
        */
        mutex.acquire();
        if(order.getPrice() >= this.curBestBidPrice){ // fill order at market price(bestBidPrice)
            fillMarketOrder(order);

        }else{ // reserve order at limit price
            //1. get index of priceIndexArray using price info.
            //2. index -> ReservedOrders object -> hash table
            //3. add [orderId, order] in the hash table.
            ReservedOrders reservedOrders = this.priceIndexArray[getIndexOfPriceIndexArray(order.getPrice())];
            reservedOrders.addOrder(order);
        }
        mutex.release();
    }

    public void cancelOrder(Order order){
        /*
        get ReservedOrders object that matches newOrder's price
        lock
        using ReservedOrders object
        check orderId of newOrder exists in hash table of the ReservedOrders object
        if exist : delete the order in hash table. cancelOrder success
        if not exists : the order is already filled. cancelOrder fail
        unlock
        */
        /*
        TODO : think of another way to delete below
         method1(above) : lock/unlock
         method2(another way) : just delete the order without locking.
         And then handle the exceptions(which can occur in cancel, fill methods)
        */
    }

    public void updatePriceAndFillOrders() throws InterruptedException {
        /*
        lock
        update bestBidPrice;
            if bestBidPrice <= prev_bestBidPrice(right before update) :
                need to fill the order (price : bestBidPrice~prev_bestBidPrice)
            else :
                nothing to do.
                finish

        get ReservedOrders objects that matches price : bestBidPrice~prev_bestBidPrice
        using ReservedOrders objects
        Fill the all order in the hash tables of the objects
        unlock
        */
        mutex.acquire();
        this.curBestAskPrice = currentPriceBuffer.getBestBidPrice();


        mutex.release();
    }

    public void fillLimitOrder(Order filledOrder){
        /*
        send the filled limit order to the data center by router
         */
    }

    public void fillMarketOrder(Order filledOrder){
        /*
        send the filled market order to the data center by router
         */

    }

    public int getIndexOfPriceIndexArray(double price){
        // price --> index
        return (int) ((price - this.startIndexPrice) / this.indexGapPrice);
    }
}