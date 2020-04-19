package nexters.moss.server.domain.cake;

import lombok.*;
import nexters.moss.server.domain.TimeProvider;

import javax.persistence.*;

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

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "sent_piece_of_cake_id")
    private Long sentPieceOfCakeId;

    @Column(name = "category_id")
    private Long categoryId;
}
