package nexters.moss.server.domain.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sent_piece_of_cakes")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SentPieceOfCake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sent_piece_of_cake_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habit habit;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "sentPieceOfCake")
    private List<ReceivedPieceOfCake> receivedPieceOfCakeList = new ArrayList<>();
}
