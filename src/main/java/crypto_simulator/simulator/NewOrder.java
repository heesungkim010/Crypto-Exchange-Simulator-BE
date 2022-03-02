package crypto_simulator.simulator;

import crypto_simulator.simulator.domain.OrderStatus;
import crypto_simulator.simulator.domain.OrderType;
import crypto_simulator.simulator.domain.Ticker;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
public class NewOrder {

    private Long id; //TODO : check if it needs to make this unique or not.

    private Ticker ticker;

    @Enumerated(EnumType.STRING)
    private OrderStatus newOrderStatus;

    @Enumerated(EnumType.STRING)
    private OrderType newOrderType;

    private double price;
    private double amount;
    private double feeRate;
    private double fee;
    private double moneyToSpend;
    private LocalDateTime openedOrderDate;

    private Long memberId;

    public NewOrder(Long id, Ticker ticker, OrderStatus filledOrderStatus, OrderType filledOrderType, double price, double amount, double feeRate, double fee, double moneyToSpend, LocalDateTime openedOrderDate, Long memberId) {
        this.id = id;
        this.ticker = ticker;
        this.newOrderStatus = filledOrderStatus;
        this.newOrderType = filledOrderType;
        this.price = price;
        this.amount = amount;
        this.feeRate = feeRate;
        this.fee = fee;
        this.moneyToSpend = moneyToSpend;
        this.openedOrderDate = openedOrderDate;
        this.memberId = memberId;
    }

}
