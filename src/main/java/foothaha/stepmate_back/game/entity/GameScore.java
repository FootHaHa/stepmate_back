package foothaha.stepmate_back.game.entity;

import foothaha.stepmate_back.base.BaseEntity;
import foothaha.stepmate_back.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game_score")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GameScore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private Integer playTimeSeconds;

    @Column(nullable = false)
    private Integer perfectCount;

    @Column(nullable = false)
    private Integer goodCount;

    @Column(nullable = false)
    private Integer missCount;

    @Column(nullable = false)
    private Integer postureScore;

    @Column(nullable = false)
    private String rank;   // S / A / B / C

    @Column(nullable = false)
    private String gameMode; // interval / highTempo / lowTempo
}
