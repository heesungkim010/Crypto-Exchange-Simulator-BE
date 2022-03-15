package crypto_simulator.simulator.web_api;

import crypto_simulator.simulator.AppConfig;
import crypto_simulator.simulator.domain.*;
import crypto_simulator.simulator.router.Router;
import crypto_simulator.simulator.service.MemberService;
import crypto_simulator.simulator.service.PositionService;
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
    private Map<String, Router> apiMeBUYRouterHashMap;
    private MemberService memberService;
    private PositionService positionService;

    @Autowired
    public OrderApiController(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.apiMeBUYRouterHashMap = appConfig.getApiMeBUYRouterHashMap();// ticker, router
        this.memberService = appConfig.getAc().getBean(MemberService.class);
        this.positionService = appConfig.getAc().getBean(PositionService.class);
    }

    @PostMapping("/api/neworder/buy")
    public OrderResponse openNewOrder(@RequestBody NewOrderRequest request) throws InterruptedException { // request received
        // open new order (BUY, CANCEL_BUY)
        /*
        MAKE BUY ORDER
        0. update the Member(balance), Position
        1. create new order with request
        2. send the order to the router( apiMeBUYRouterHashMap.get(ticker) )
        3. return opening order result
        MAKE CANCEL_BUY ORDER
        1. create cancel order with request
        2. send the order to the router( apiMeBUYRouterHashMap.get(ticker) )
        3. return opening order result
        */
        Order newOrder;
        if (request.getNewOrderType() == OrderType.BUY){
            newOrder = request.getNewOrder();
            memberService.updateUsdBalanceOpen(request.getMemberId(), newOrder);
            positionService.updatePositionOpen(memberService.findById(request.getMemberId()), newOrder);
        }else{

        }//TODO : START HERE
        apiMeBUYRouterHashMap.get("btc").send(newOrder); // send order

        return new OrderResponse("111",true);
    }

    @PostMapping("/api/neworder/sell")
    public OrderResponse cancelOrder(@RequestBody NewOrderRequest request) throws InterruptedException { // request received
        // open new order (SELL, CANCEL_SELL)
        /*
        MAKE SELL ORDER
        0. update the Member(balance), Position
        1. create new order with request
        2. send the order to the router( apiMeBUYRouterHashMap.get(ticker) )
        3. return opening order result
        MAKE CANCEL_SELL ORDER
        1. create cancel order with request
        2. send the order to the router( apiMeBUYRouterHashMap.get(ticker) )
        3. return opening order result
        */
        //apiMeRouterHashMap.get("btc").send(request.getNewOrder()); // send order
        return new OrderResponse("111",true);
    }

    /*
    Order order = new Order(Ticker.BTCUSD, OrderStatus.OPEN, OrderType.BUY,
        40000, 10, 0.1, 20, 90000, 0,LocalDateTime.now(),1L);
     */

    @Data
    class OrderResponse{
        private String memberId;
        private boolean openNewOrderResult;

        public OrderResponse(String id, boolean didOpen){
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

        private Long idToCancel; // ONLY FOR CANCEL ORDER
        private Long memberId;

        private Order getNewOrder(){
            return new Order(this.ticker, this.newOrderStatus, this.newOrderType,
                    this.price, this.amount, this.feeRate, this.fee, this.moneyToSpend,
                    this.moneyToGet, this.memberId);
        }

        private Order getNewCancelOrder(){
            return new Order(this.idToCancel, this.ticker, this.newOrderStatus, this.newOrderType,
                    this.price, this.amount, this.feeRate, this.fee, this.moneyToSpend,
                    this.moneyToGet, this.memberId);
        }
    }
}