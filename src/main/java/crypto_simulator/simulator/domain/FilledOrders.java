package crypto_simulator.simulator.domain;

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

    private double price;
    private double amount;
    private double feeRate;
    private double fee;
    private double moneyToSpend;
    private LocalDateTime filledOrderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberInFilledOrders;

    public static FilledOrders createFilledOrder(Ticker ticker){
        //TODO: get parameters by DTO : LATER!
        FilledOrders filledOrder = new FilledOrders();
        filledOrder.setTicker(ticker);

        return filledOrder;
    }
}