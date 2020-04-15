package nexters.moss.server.domain.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "whole_cakes")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WholeCake extends TimeProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "whole_cake_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(name = "category_id")
    private Long categoryId;

    public WholeCake(
            Long userId,
            Long habitId,
            Long categoryId
    ) {
        this.user = User.builder()
                .id(userId)
                .build();
        this.habit = new Habit(habitId);
        this.categoryId = categoryId;
    }
}
