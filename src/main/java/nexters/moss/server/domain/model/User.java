package nexters.moss.server.domain.model;

import lombok.*;
import org.hibernate.annotations.OnDeleteAction;

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

    @OneToMany(mappedBy = "user")
    @org.hibernate.annotations.OrderBy(clause = "habit_order ASC")
    private List<Habit> habits = new ArrayList<>();
}
