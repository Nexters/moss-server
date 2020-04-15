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
public class User extends TimeProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "social_id")
    private Long socialId;

    @Column(name = "habikery_token")
    @Setter
    private String habikeryToken;

    @Column(name = "nickname")
    private String nickname;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @Builder.Default
    private List<Report> reports = new ArrayList<>();

    public void reported(String reason) {
        reports.add(
                Report.builder()
                        .reason(reason)
                        .build()
        );
    }
}
