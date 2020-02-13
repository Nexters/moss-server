package nexters.moss.server.domain.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "description")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "description_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(name = "receive_piece_of_cake")
    private String receivePieceOfCake;

    @Column(name = "diary")
    private String diary;

    @Column(name = "cake_history")
    private String cakeHistory;
}
