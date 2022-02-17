package crypto_simulator.simulator.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private Long id2;

    //Account information
    private String userId;
    private String password;

    //Balance
    private double totalMoney; // usuable + currently Ordered + positioned Asset valued in money
    private double usuableMoney;
    private double curOrderedMoney;
    private double positionedAssetInMoney;

    @OneToMany(mappedBy = "memberInOrders")
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "memberInCurPosition")
    private List<CurPosition> curPositions = new ArrayList<>();

}