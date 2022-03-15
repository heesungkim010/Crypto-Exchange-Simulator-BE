package crypto_simulator.simulator.data_center;

import crypto_simulator.simulator.domain.*;
import crypto_simulator.simulator.router.Router;
import crypto_simulator.simulator.service.FilledOrderService;
import crypto_simulator.simulator.service.MemberService;
import crypto_simulator.simulator.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


public class DataCenter implements Runnable{
    private MemberService memberService;
    private FilledOrderService filledOrderService; // SpringBean
    private PositionService positionService;
    private Router meToDataCenterRouter;
    private String ticker;

    public DataCenter(MemberService memberService, FilledOrderService filledOrderService, PositionService positionService, Router meToDataCenterRouter, String ticker) {
        this.memberService = memberService;
        this.filledOrderService = filledOrderService;
        this.positionService = positionService;
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
                    --> ignore these two. or notify the order is already filled
                */
                Order order = this.meToDataCenterRouter.receive();

                OrderType orderType = order.getNewOrderType();
                OrderStatus newOrderStatus = order.getNewOrderStatus();

                if(newOrderStatus == OrderStatus.OPEN){ // if failed -> ignore
                    order.setNewOrderStatus(OrderStatus.FILLED);
                    if(orderType == OrderType.BUY){
                        fillBuyOrder(order);

                    }else if(orderType == OrderType.CANCEL_BUY){
                        System.out.println("data center cancel buy");
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

    /*
    filledOrderService : save Filled
    member : updateBalance Open/Filled/Cancelled
    position : updatePosition Open/Filled/Cancelled

    Position position = positionService.findByMemberIdTicker(savedId, Ticker.BTCUSD);

    */

    void fillBuyOrder(Order order){
    /*
        filledOrderService : save Filled
        memberService : updateBalance Open/Filled/Cancelled
        positionService : updatePosition Open/Filled/Cancelled
        notify the order is filled.
     */
        Long memberId = order.getMemberId();
        Member member = memberService.findById(memberId);

        filledOrderService.saveFilledOrder(order, member);
        memberService.updateUsdBalanceFilled(memberId, order);
        positionService.updatePositionFilled(member, order);
        //TODO : NOTIFY THE ORDER IS FILLED
    }

    void fillSellOrder(Order order){
    /*  SAME AS fillBuyOrder above.
        filledOrderService : save Filled
        memberService : updateBalance Open/Filled/Cancelled
        positionService : updatePosition Open/Filled/Cancelled
        notify the order is filled.
     */
        //TODO : CHECK if method works with matching engine_SELL
        Long memberId = order.getMemberId();
        Member member = memberService.findById(memberId);

        filledOrderService.saveFilledOrder(order, member);
        memberService.updateUsdBalanceFilled(memberId, order);
        positionService.updatePositionFilled(member, order);
        //TODO : NOTIFY THE ORDER IS FILLED
    }

    void fillCancelBuyOrder(Order order){
    /*

        memberService : updateBalance Open/Filled/Cancelled
        positionService : updatePosition Open/Filled/Cancelled
        notify the order is filled.
     */
        Long memberId = order.getMemberId();
        Member member = memberService.findById(memberId);

        System.out.println("datacenter fillCancelBuyOrder");
        memberService.updateUsdBalanceCanceled(memberId, order);
        positionService.updatePositionCanceled(member, order);
        //TODO : NOTIFY THE ORDER IS FILLED(CANCELED)
    }

    void fillCancelSellOrder(Order order){
    /*

        member : updateBalance Open/Filled/Cancelled
        position : updatePosition Open/Filled/Cancelled
         notify the order is filled.
     */
        Long memberId = order.getMemberId();
        Member member = memberService.findById(memberId);

        memberService.updateUsdBalanceCanceled(memberId, order);
        positionService.updatePositionCanceled(member, order);
        //TODO : NOTIFY THE ORDER IS FILLED(CANCELED)

    }
}