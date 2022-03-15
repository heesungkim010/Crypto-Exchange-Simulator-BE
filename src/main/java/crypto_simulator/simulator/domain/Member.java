package crypto_simulator.simulator.domain;

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

    public Member() {
    }

    public Member(String userId, String password, double usuableMoney, double curOrderedMoney) {
        this.userId = userId;
        this.password = password;
        this.usuableMoney = usuableMoney;
        this.curOrderedMoney = curOrderedMoney;
    }

    public void updateUsdBalanceFilled(Order order){
        if (order.getNewOrderType() == OrderType.BUY){
            this.curOrderedMoney -= order.getMoneyToSpend();
        }else{ // OrderType.SELL
            this.usuableMoney -= order.getMoneyToGet();
        }
    }

    public void updateUsdBalanceOpen(Order order){
        if (order.getNewOrderType() == OrderType.BUY){
            this.usuableMoney -= order.getMoneyToSpend();
            this.curOrderedMoney += order.getMoneyToSpend();
        }else{ // OrderType.SELL
            //nothing to do in usd balance
        }
    }

    public void updateUsdBalanceCanceled(Order order){
        if (order.getNewOrderType() == OrderType.CANCEL_BUY){
            this.usuableMoney += order.getMoneyToSpend();
            this.curOrderedMoney -= order.getMoneyToSpend();
        }else{ // OrderType.SELL
            //nothing to do in usd balance
        }
    }

}