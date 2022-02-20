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

    //Balance
    private double totalMoney; // usuable + currently Ordered + positioned Asset valued in money
    private double usuableMoney;
    private double curOrderedMoney;
    private double positionedAssetInMoney;

    @OneToMany(mappedBy = "memberInFilledOrders")
    private List<FilledOrders> filledOrders = new ArrayList<>();

    @OneToMany(mappedBy = "memberInPosition")
    private List<Position> Positions = new ArrayList<>();

}