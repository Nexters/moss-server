package nexters.moss.server.domain.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "received_piece_of_cakes")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceivedPieceOfCake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "received_piece_of_cake_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "sent_piece_of_cake_id", nullable = false)
    private SentPieceOfCake sentPieceOfCake;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
