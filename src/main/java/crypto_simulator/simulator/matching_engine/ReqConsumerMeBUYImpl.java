package crypto_simulator.simulator.matching_engine;

import crypto_simulator.simulator.domain.Order;
import crypto_simulator.simulator.domain.OrderType;
import crypto_simulator.simulator.router.Router;

public class ReqConsumerMeBUYImpl implements Runnable, ReqConsumerMe {
    /*
    gets order from router
    checks order type
    call methods in matchingEngine accordingly.
    runs in thread
     */
    private String ticker;
    private MatchingEngineBUY matchingEngineBUY;
    private Router apiMeRouter;

    public ReqConsumerMeBUYImpl(String ticker, MatchingEngineBUY me, Router apiMeRouter) {
        this.ticker = ticker;
        this.matchingEngineBUY = me;
        this.apiMeRouter = apiMeRouter;
    }

    @Override
    public void run() {
        while(true){
            try {
                Order order = this.apiMeRouter.receive();
                OrderType orderType = order.getNewOrderType();

                if (orderType == OrderType.BUY){
                    matchingEngineBUY.openOrder(order);

                }else{ //(orderType == OrderType.CANCEL_BUY)
                    System.out.println("reqConsumerImpl cancel order");
                    matchingEngineBUY.cancelOrder(order);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
