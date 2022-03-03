package crypto_simulator.simulator.domain;

import crypto_simulator.simulator.NewOrder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1,
        allocationSize =50 )
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "MEMBER_SEQ_GENERATOR")
    @Column(name = "member_id")
    private Long id;

    //Account information
    private String userId;
    private String password;

    //Balance : usd(usuable + curOrdered) + positions
    private double usuableMoney;
    private double curOrderedMoney;

    @OneToMany(mappedBy = "memberInFilledOrders")
    private List<FilledOrders> filledOrders = new ArrayList<>();

    @OneToMany(mappedBy = "memberInPosition")
    private List<Position> Positions = new ArrayList<>();

    public void updateUsdBalanceFilled(NewOrder newOrder){
        if (newOrder.getNewOrderType() == OrderType.BUY){
            this.curOrderedMoney -= newOrder.getMoneyToSpend();
        }else{ // OrderType.SELL
            this.usuableMoney -= newOrder.getMoneyToGet();
        }
    }

    public void updateUsdBalanceOpen(NewOrder newOrder){
        if (newOrder.getNewOrderType() == OrderType.BUY){
            this.usuableMoney -= newOrder.getMoneyToSpend();
            this.curOrderedMoney += newOrder.getMoneyToSpend();
        }else{ // OrderType.SELL
            //nothing to do in usd balance
        }
    }

    public void updateUsdBalanceCancelled(NewOrder newOrder){
        if (newOrder.getNewOrderType() == OrderType.BUY){
            this.usuableMoney += newOrder.getMoneyToSpend();
            this.curOrderedMoney -= newOrder.getMoneyToSpend();
        }else{ // OrderType.SELL

        }
    }

}