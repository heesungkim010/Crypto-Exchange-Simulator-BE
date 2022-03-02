package crypto_simulator.simulator.domain;

import crypto_simulator.simulator.NewOrder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@SequenceGenerator(
        name = "FILLED_ORDERS_SEQ_GENERATOR",
        sequenceName = "FILLED_ORDERS_SEQ",
        initialValue = 1,
        allocationSize =1 )

public class FilledOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "FILLED_ORDERS_SEQ_GENERATOR")
    @Column(name = "filled_orders_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Ticker ticker;

    @Enumerated(EnumType.STRING)
    private OrderStatus filledOrderStatus;

    @Enumerated(EnumType.STRING)
    private OrderType filledOrderType;

    private Long newOrderId;
    private double price;
    private double amount;
    private double feeRate;
    private double fee;
    private double moneyToSpend;
    private LocalDateTime filledOrderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberInFilledOrders;

    public static FilledOrders createFilledOrder(NewOrder newOrder, Member newMember){
        //TODO: think of another way of cloning object without one~two varible
        FilledOrders filledOrder = new FilledOrders();

        filledOrder.setTicker(newOrder.getTicker());
        filledOrder.setFilledOrderStatus(newOrder.getNewOrderStatus());
        filledOrder.setFilledOrderType(newOrder.getNewOrderType());
        filledOrder.setNewOrderId(newOrder.getId());

        filledOrder.setPrice(newOrder.getPrice());
        filledOrder.setAmount(newOrder.getAmount());
        filledOrder.setFeeRate(newOrder.getFeeRate());
        filledOrder.setFee(newOrder.getFee());
        filledOrder.setMoneyToSpend(newOrder.getMoneyToSpend());
        filledOrder.setFilledOrderDate(newOrder.getOpenedOrderDate());

        filledOrder.setMemberInFilledOrders(newMember);

        return filledOrder;
    }
}