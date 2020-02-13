package nexters.moss.server.domain.model;


import lombok.*;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.HabitType;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "habit_name")
    private HabitType habitType;

    @Enumerated(EnumType.STRING)
    @Column(name = "cake_name")
    private CakeType cakeType;
}
