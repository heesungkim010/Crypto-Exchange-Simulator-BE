package crypto_simulator.simulator.data_center;

import crypto_simulator.simulator.domain.Order;
import crypto_simulator.simulator.domain.OrderStatus;
import crypto_simulator.simulator.domain.OrderType;
import crypto_simulator.simulator.router.Router;
import crypto_simulator.simulator.service.FilledOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class DataCenter implements Runnable{
    private FilledOrderService filledOrderService; // SpringBean
    private Router meToDataCenterRouter;
    private String ticker;

    public DataCenter(FilledOrderService filledOrderService, Router meToDataCenterRouter, String ticker) {
        this.filledOrderService = filledOrderService;
        this.meToDataCenterRouter = meToDataCenterRouter;
        this.ticker = ticker;
    }
    /*
    1. receive order from router
    2. check order type(buy, sell, cancel_buy, cancel_sell), change order type(eg. filled)
    3. call method to deal with the order.
       (should consider all domains related to the orders)
     */
    @Override
    public void run() {
        while(true){
            try {
                /*
                orderType : buy, sell, cancel_buy, cancel_sell
                orderStatus : open, canceled, filled, failed
                Arrived orderStatus : open  /  failed (cancel failed)
                change order type : open -> filled. failed -> failed.

                6 cases here.
                4 (buy, sell, cancel_buy, cancel_sell ) * 1 (open->filled)
                    --> buy / sell filled :
                        save filled order, update member balance/position.
                        notify the order is filled.

                    --> cancel_buy / cancel_sell filled :
                        update member balance/position.
                        notify the order is canceled.

                2 (cancel_buy, cancel_sell ) * 1 (failed)
                    --> ignore these two.
                */
                Order order = this.meToDataCenterRouter.receive();

                OrderType orderType = order.getNewOrderType();
                OrderStatus newOrderStatus = order.getNewOrderStatus();

                if(newOrderStatus == OrderStatus.OPEN){ // if failed -> ignore
                    order.setNewOrderStatus(OrderStatus.FILLED);
                    if(orderType == OrderType.BUY){
                        fillBuyOrder(order);

                    }else if(orderType == OrderType.CANCEL_BUY){
                        fillCancelBuyOrder(order);

                    }else if(orderType == OrderType.SELL){
                        fillSellOrder(order);

                    }else if(orderType == OrderType.CANCEL_SELL){
                        fillCancelSellOrder(order);
                    }else{
                        //TODO : NOTIFY ERROR
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void fillBuyOrder(Order order){
        System.out.println("fill buy");
    }

    void fillSellOrder(Order order){
        System.out.println("fill sell");
    }

    void fillCancelBuyOrder(Order order){
        System.out.println("fill cancel buy");
    }

    void fillCancelSellOrder(Order order){
        System.out.println("fill cancel sell");
    }
}