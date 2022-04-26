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
public class FilledOrder {
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
    private Member memberInFilledOrder;

    public static FilledOrder createFilledOrder(Order order, Member newMember){
        //TODO: think of another way of cloning object without one~two varible
        FilledOrder filledOrder = new FilledOrder();

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

        filledOrder.setMemberInFilledOrder(newMember);

        return filledOrder;
    }
}