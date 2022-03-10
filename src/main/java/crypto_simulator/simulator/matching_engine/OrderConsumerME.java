package crypto_simulator.simulator.matching_engine;

import crypto_simulator.simulator.domain.Order;
import crypto_simulator.simulator.router.Router;

public class OrderConsumerME implements Runnable{
    /*
    gets order from router
    checks order type
    call methods in matchingEngine accordingly.
    runs in thread
     */
    private String ticker;
    private MatchingEngine matchingEngine;
    private Router apiMeRouter;

    public OrderConsumerME(String ticker, MatchingEngine me,Router apiMeRouter) {
        this.ticker = ticker;
        this.matchingEngine = me;
        this.apiMeRouter = apiMeRouter;
    }

    @Override
    public void run() {
        while(true){
            try {
                Order order = this.apiMeRouter.receive();
                matchingEngine.openOrder(order);
                //System.out.println(order);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
