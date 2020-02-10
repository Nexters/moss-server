package nexters.moss.server.domain.model;

import lombok.*;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.HabitType;

import javax.persistence.*;

@Entity
@Table(name = "habits")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "habit_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "isActivated", columnDefinition = "boolean default false")
    private Boolean isActivated;

    @Column(name = "isFirstCheck", columnDefinition = "boolean default false")
    private Boolean isFirstCheck;
}
