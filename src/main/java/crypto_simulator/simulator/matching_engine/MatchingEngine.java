package crypto_simulator.simulator.matching_engine;

import crypto_simulator.simulator.domain.NewOrder;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class MatchingEngine {

    private String ticker;
    private ExternalPriceInfoReceiver priceInfoReceiver;
    private CurrentPriceBuffer currentPriceBuffer;
    private Long bestBidPrice;
    private Long bestAskPrice;
    private Semaphore mutex;
    Map<String, NewOrder> hashMap = new ConcurrentHashMap<>();

    public MatchingEngine(String ticker) throws InterruptedException {
        this.ticker = ticker;
        this.currentPriceBuffer = new CurrentPriceBuffer(ticker);
        this.priceInfoReceiver = new ExternalPriceInfoReceiver(ticker, this.currentPriceBuffer);

        this.mutex = new Semaphore(1, true);
    }

    // TODO : init and re-init every 12 hours(because of binance websocket limitation rules)
    //       re-init when websocket error

    // currentPriceBuffer.getBestBidPrice()
    // currentPriceBuffer.getBestAskPrice()

    public void openOrder(NewOrder newOrder) throws InterruptedException {
        /*
        lock
        check bestBidPrice;
        do market filled or reserve order at limit price
        unlock
        */
        mutex.acquire();
        if(newOrder.getPrice() >= this.bestBidPrice){ // do market filled

        }else{ // reserve order at limit price

        }
        mutex.release();
    }

    public void cancelOrder(NewOrder newOrder){
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

    public void updatePriceAndFillOrders(){
        /*
        lock
        update bestBidPrice;
            if bestBidPrice <= prev_bestBidPrice(right before update) :
                need to fill the orders (price : bestBidPrice~prev_bestBidPrice)
            else :
                nothing to do.
                finish

        get ReservedOrders objects that matches price : bestBidPrice~prev_bestBidPrice
        using ReservedOrders objects
        Fill the all orders in the hash tables of the objects
        unlock
        */
    }

    public void fillOrder(NewOrder filledOrder){
        /*
        send the filled order to the data center by router


         */
    }
}
