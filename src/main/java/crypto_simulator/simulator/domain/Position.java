package crypto_simulator.simulator.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@SequenceGenerator(
        name = "POSITION_SEQ_GENERATOR",
        sequenceName = "POSITION_SEQ",
        initialValue = 1,
        allocationSize =1 )
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "POSITION_SEQ_GENERATOR")
    @Column(name = "position_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Ticker ticker;

    private double amount;

    private double avgBoughtPrice;
    private double availableAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberInPosition;

    public static Position createPosition(Member member, Ticker ticker){
        Position position = new Position();
        position.setTicker(ticker);
        position.setAmount(0);
        position.setAvgBoughtPrice(0);
        position.setAvailableAmount(0);

        position.setMemberInPosition(member);
        //TODO : check if need to initiate in domain Member.java
        // domain Member : private List<Position> Positions = new ArrayList<>();
        // if need to add position in ArrayList.

        return position;
    }

    public void updatePositionFilled(Order order){
        if (order.getNewOrderType() == OrderType.BUY){
            this.avgBoughtPrice = (this.avgBoughtPrice * this.amount
                    + order.getPrice() * order.getAmount() )/(this.amount + order.getAmount());
            this.amount += order.getAmount();
            this.availableAmount += order.getAmount();
        }else{ // OrderType.SELL
            this.amount -= order.getAmount();
            this.availableAmount -= order.getAmount();
        }
    }

    public void updatePositionOpen(Order order){
        if (order.getNewOrderType() == OrderType.BUY){
            //nothing to do when buy
        }else{ // OrderType.SELL
            this.availableAmount -= order.getAmount();
        }
    }

    public void updatePositionCanceled(Order order) {
        if (order.getNewOrderType() == OrderType.CANCEL_BUY) {
            //nothing to do when buy
        } else { // OrderType.CANCEL_SELL
            this.availableAmount += order.getAmount();
        }
    }

}
