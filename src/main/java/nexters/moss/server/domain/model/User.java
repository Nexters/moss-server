package nexters.moss.server.domain.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "social_id")
    private String socialId;

    @Column(name = "account_token")
    private String accountToken;

    @Column(name = "nickname")
    private String nickname;

    // TODO: modify EAGER to LAZY
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<HabitRecord> habitRecords = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<SentPieceOfCake> sentPieceOfCakes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ReceivedPieceOfCake> receivedPieceOfCakes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<WholeCake> wholeCakes = new ArrayList<>();
}
