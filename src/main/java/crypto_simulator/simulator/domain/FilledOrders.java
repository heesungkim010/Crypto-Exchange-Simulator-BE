package crypto_simulator.simulator.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@SequenceGenerator(
        name = "FILLED_SEQ_GENERATOR",
        sequenceName = "FILLED_SEQ",
        initialValue = 1,
        allocationSize =50 )
public class FilledOrders {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "FILLED_SEQ_GENERATOR")
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
    private double moneyToGet;
    private LocalDateTime filledOrderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberInFilledOrders;

    public static FilledOrders createFilledOrder(Order order, Member newMember){
        //TODO: think of another way of cloning object without one~two varible
        FilledOrders filledOrder = new FilledOrders();

        filledOrder.setTicker(order.getTicker());
        filledOrder.setFilledOrderStatus(order.getNewOrderStatus());
        filledOrder.setFilledOrderType(order.getNewOrderType());
        filledOrder.setNewOrderId(order.getId());

        filledOrder.setPrice(order.getPrice());
        filledOrder.setAmount(order.getAmount());
        filledOrder.setFeeRate(order.getFeeRate());
        filledOrder.setFee(order.getFee());
        filledOrder.setMoneyToSpend(order.getMoneyToSpend());
        filledOrder.setMoneyToGet(order.getMoneyToGet());
        filledOrder.setFilledOrderDate(order.getOpenedOrderDate());

        filledOrder.setMemberInFilledOrders(newMember);

        return filledOrder;
    }
}