package nexters.moss.server.domain.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "received_piece_of_cakes")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceivedPieceOfCake extends TimeProvider {
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

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public ReceivedPieceOfCake(
            Long userId,
            Long sentPieceOfCakeId,
            Long categoryId
    ) {
        this.user = User.builder()
                .id(userId)
                .build();
        this.sentPieceOfCake = SentPieceOfCake.builder()
                .id(sentPieceOfCakeId)
                .build();
        this.category = Category.builder()
                .id(categoryId)
                .build();
    }
}
