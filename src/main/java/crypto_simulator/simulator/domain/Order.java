package crypto_simulator.simulator.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@SequenceGenerator(
        name = "ORDER_SEQ_GENERATOR",
        sequenceName = "ORDER_SEQ",
        initialValue = 1,
        allocationSize =50 )
@Getter @Setter
public class Order {
    private static AtomicLong atomicLong = new AtomicLong(0);
    private Long id;
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
    private double moneyToGet;
    private LocalDateTime openedOrderDate;

    private Long memberId;

    public Order(Ticker ticker, OrderStatus filledOrderStatus, OrderType filledOrderType, double price, double amount, double feeRate, double fee, double moneyToSpend, double moneyToGet, LocalDateTime openedOrderDate, Long memberId) {
        this.id = atomicLong.incrementAndGet();
        this.ticker = ticker;
        this.newOrderStatus = filledOrderStatus;
        this.newOrderType = filledOrderType;
        this.price = price;
        this.amount = amount;
        this.feeRate = feeRate;
        this.fee = fee;
        this.moneyToSpend = moneyToSpend;
        this.moneyToGet = moneyToGet;
        this.openedOrderDate = openedOrderDate;
        this.memberId = memberId;
    }
}