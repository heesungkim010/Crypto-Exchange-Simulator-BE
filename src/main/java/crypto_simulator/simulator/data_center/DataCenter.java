package crypto_simulator.simulator.data_center;

import crypto_simulator.simulator.router.Router;
import crypto_simulator.simulator.service.FilledOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class DataCenter {
    private FilledOrderService filledOrderService; // SpringBean
    private Router meToDataCenterRouter;
    private String ticker;

    public DataCenter(FilledOrderService filledOrderService, Router meToDataCenterRouter, String ticker) {
        this.filledOrderService = filledOrderService;
        this.meToDataCenterRouter = meToDataCenterRouter;
        this.ticker = ticker;
    }

    /*
    Do the followings iteratively.
    1. receive order from router
    2. check order type(buy, sell, cancel_buy, cancel_sell), change order type(eg. filled)
    3. call method to deal with the order.
       (should consider all domains related to the orders)
     */

}