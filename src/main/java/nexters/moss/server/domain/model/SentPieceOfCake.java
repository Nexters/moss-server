package nexters.moss.server.domain.model;

import lombok.*;
import nexters.moss.server.application.dto.cake.CreateNewCakeRequest;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sent_piece_of_cakes")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SentPieceOfCake extends TimeProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sent_piece_of_cake_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "note")
    @Length(min = 3, max = 14)
    private String note;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceivedPieceOfCake> receivedPieceOfCakeList = new ArrayList<>();
}
