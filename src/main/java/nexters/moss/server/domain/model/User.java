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
    private String habikeryToken;

    @Column(name = "nickname")
    private String nickname;

    @OneToMany(mappedBy = "user")
    private List<Habit> habits = new ArrayList<>();
}
