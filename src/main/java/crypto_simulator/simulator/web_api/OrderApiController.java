package crypto_simulator.simulator.web_api;

import crypto_simulator.simulator.AppConfig;
import crypto_simulator.simulator.domain.*;
import crypto_simulator.simulator.router.Router;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@Slf4j
@Component
public class OrderApiController {
    private static AtomicLong atomicLong = new AtomicLong(0);
    private final AppConfig appConfig;
    private Map<String, Router> apiMeRouterHashMap;

    @Autowired
    public OrderApiController(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.apiMeRouterHashMap = appConfig.getApiMeRouterHashMap();// ticker, router
    }

    @PostMapping("/api/neworder")
    public NewOrderResponse openNewOrder(@RequestBody NewOrderRequest request) throws InterruptedException { // request received
        // open new order
        // 1. create new order with request
        // 2. send the order to the router( apiMeRouterHashMap.get(ticker) )
        // 3. return opening order result
        log.info("{}",request);
        log.info("price check : {}", request.getPrice());
        log.info("ticker check : {}", request.ticker);

        apiMeRouterHashMap.get("btc").send(request.getNewOrder()); // send order
        return new NewOrderResponse("111",true);
    }
    /*
    Order order = new Order(Ticker.BTCUSD, OrderStatus.OPEN, OrderType.BUY,
        40000, 10, 0.1, 20, 90000, 0,LocalDateTime.now(),1L);
     */


    @Data
    class NewOrderResponse{
        private String memberId;
        private boolean openNewOrderResult;

        public NewOrderResponse(String id, boolean didOpen){
            this.memberId = id;
            this.openNewOrderResult = didOpen;
        }
    }

    @Data
    static class NewOrderRequest{

        @Enumerated(EnumType.STRING)
        private Ticker ticker; // BTCUSD, ETHUSD

        @Enumerated(EnumType.STRING)
        private OrderStatus newOrderStatus; //OPEN, CANCELED, FILLED

        @Enumerated(EnumType.STRING)
        private OrderType newOrderType; // BUY, SELL

        private double price;
        private double amount;
        private double feeRate;
        private double fee;
        private double moneyToSpend;
        private double moneyToGet;

        private Long memberId;

        private Order getNewOrder(){
            return new Order(this.ticker, this.newOrderStatus, this.newOrderType,
                    this.price, this.amount, this.feeRate, this.fee, this.moneyToSpend,
                    this.moneyToGet, this.memberId);
        }
    }
}