package nexters.moss.server.domain.cake;

import lombok.*;
import nexters.moss.server.domain.TimeProvider;

import javax.persistence.*;

@Entity
@Table(name = "whole_cakes")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WholeCake extends TimeProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "whole_cake_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "habit_id")
    private Long habitId;

    @Column(name = "category_id")
    private Long categoryId;

    @Builder
    public WholeCake(
            Long userId,
            Long habitId,
            Long categoryId
    ) {
        this.userId = userId;
        this.habitId = habitId;
        this.categoryId = categoryId;
    }
}
