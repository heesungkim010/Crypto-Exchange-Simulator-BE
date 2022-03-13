package crypto_simulator.simulator.matching_engine;

import crypto_simulator.simulator.domain.Order;
import crypto_simulator.simulator.router.Router;

import java.util.Map;
import java.util.concurrent.Semaphore;

public class MatchingEngineBUY implements Runnable{

    private String ticker;
    private ExternalPriceInfoReceiver priceInfoReceiver;
    //private double curBestBidPrice;
    //private double prevBestBidPrice;
    private double curBestAskPrice;
    private double prevBestAskPrice;

    private Semaphore mutex;
    private Semaphore[] mutexArray;
    private ReservedOrders[] priceIndexArrayBuy;
    private double startIndexPrice;
    private double endIndexPrice;
    private double indexGapPrice;

    public MatchingEngineBUY(String ticker, double[] indexPriceList, Router apiMeRouter,
                             ExternalPriceInfoReceiver externalPriceInfoReceiver
                            ) throws InterruptedException {
        this.ticker = ticker;
        this.priceInfoReceiver = externalPriceInfoReceiver;

        //Order ConsumerMe
        this.prevBestAskPrice = -1;
        this.mutex = new Semaphore(1, true);

        this.startIndexPrice = indexPriceList[0];
        this.endIndexPrice = indexPriceList[1];
        this.indexGapPrice = indexPriceList[2];

        this.mutexArray = new Semaphore[(int)indexPriceList[3]];
        this.priceIndexArrayBuy = new ReservedOrders[(int)indexPriceList[3]];

        for(int i = 0; i < (int)indexPriceList[3]; i++){
            this.priceIndexArrayBuy[i] = new ReservedOrders();
            this.mutexArray[i] = new Semaphore(1, true);
        }
        Thread threadUpdateAndFill = new Thread(this, "updateAndFill");
        threadUpdateAndFill.start();
    }

    // TODO : init and re-init every 12 hours(because of binance websocket limitation rules)
    //       re-init when websocket error

    // currentPriceBuffer.getBestBidPrice()
    // currentPriceBuffer.getBestAskPrice()

    public void openOrder(Order order) throws InterruptedException {
        /*
        order type(BUY)

        lock
        check bestAskPrice;
        do market filled or reserve order at limit price
        unlock
        */
        mutex.acquire();
        if(order.getPrice() >= this.curBestAskPrice){ // fill order at market price(bestAskPrice)
            fillMarketOrder(order);

        }else{ // reserve order at limit price
            //1. get index of priceIndexArray using price info.
            //2. index -> ReservedOrders object -> hash table
            //3. add [orderId, order] in the hash table.
            ReservedOrders reservedOrdersImpl =
                    this.priceIndexArrayBuy[getIndexOfPriceIndexArray(order.getPrice())];
            reservedOrdersImpl.addOrder(order);
        }
        mutex.release();
    }

    public boolean cancelOrder(Order order) throws InterruptedException {
        /*

        get ReservedOrders object that matches newOrder's price
        lock
        using ReservedOrders object
        check orderId of newOrder exists in hash table of the ReservedOrders object
        if exist : delete the order in hash table. cancelOrder success
        if not exists : the order is already filled. cancelOrder fail
        unlock
        */
        mutex.acquire();
        // reserveOrder at limit price
        //1. get index of priceIndexArray using price info.
        //2. index -> ReservedOrders object -> hash table
        //3. remove [orderId, order] in the hash table.
        ReservedOrders reservedOrdersImpl =
                this.priceIndexArrayBuy[getIndexOfPriceIndexArray(order.getPrice())];
        boolean result = reservedOrdersImpl.cancelOrder(order.getId());

        mutex.release();
        return result;
        /*
        TODO : think of another way to delete order
         method1(above) : lock/unlock
         method2(another way) : just delete the order without locking.
         And then handle the exceptions(which can occur in cancel, fill methods)
         method3 : use atomic reference array for priceIndexArray
        */
    }

    @Override
    public void run() {
        while(true){
            try {
                updatePriceAndFillBuyOrders();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void updatePriceAndFillBuyOrders() throws InterruptedException {
        /*
        lock
        update bestAsk;

            if bestAskPrice <= prevBestAskPrice(right before update) :
                need to fill the order (price : bestAskPrice~prev_bestAskPrice)
            else :
                nothing to do.
                finish
        get ReservedOrders objects that matches price : bestAskPrice~prevBestAskPrice
        using ReservedOrders objects
        Fill the all order in the hash tables of the objects

        set prevBestAskPrice
        send current price info by websocket(to front-end server).
        unlock
        */
        mutex.acquire();
        this.curBestAskPrice = priceInfoReceiver.getBestAskPrice();
        System.out.println(this.curBestAskPrice);
        if (this.curBestAskPrice < this.prevBestAskPrice){
            //fill the order (price : bestAskPrice~prev_bestAskPrice)
            for(int i = getIndexOfPriceIndexArray(this.prevBestAskPrice);
                i >= getIndexOfPriceIndexArray(this.curBestAskPrice); i--){
                fillLimitOrders( this.priceIndexArrayBuy[i]);
            }
        }
        this.prevBestAskPrice = this.curBestAskPrice;
        //TODO : send cur price info by web socket.
        mutex.release();
    }

    public void fillLimitOrders(ReservedOrders reservedOrdersImpl){
        /*
        for all orders in hashtable of reservedOrders
            : (1) send the filled limit orders to the data center by router
        erase all the elements in hashtable
         */
        for(Map.Entry<Long, Order> elem : reservedOrdersImpl.getHashMap().entrySet()){
            //TODO : set router ( ME - Data Center. ManyToONE) ... LIMIT ORDER + MARKET ORDER
        }
        reservedOrdersImpl.getHashMap().clear();
    }

    public void fillMarketOrder(Order filledOrder){
        /*
        send the filled market order to the data center by router
         */
        filledOrder.setPrice(this.curBestAskPrice); // change the order price to market price.
        //TODO : set router  ( ME - Data Center. ManyToONE) ... LIMIT ORDER + MARKET ORDER
    }

    public int getIndexOfPriceIndexArray(double price){
        // price --> index
        return (int) ((price - this.startIndexPrice) / this.indexGapPrice);
    }
}