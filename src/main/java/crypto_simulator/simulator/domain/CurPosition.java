package crypto_simulator.simulator.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class CurPosition {
    @Id
    @GeneratedValue
    @Column(name = "curposition_id")
    private Long id;


    @Enumerated(EnumType.STRING)
    private Ticker ticker;

    private double amount;

    private double avgBoughtPrice;
    private double availableAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberInCurPosition;
}
