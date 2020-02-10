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

    @Enumerated(EnumType.STRING)
    @Column(name = "habit_type")
    private HabitType habitType;

    @Enumerated(EnumType.STRING)
    @Column(name = "cake_type")
    private CakeType cakeType;

    @Column(name = "whole_cake_image_path")
    private String wholeCakeImagePath;

    @Column(name = "piece_of_cake_image_path")
    private String pieceOfCakeImagePath;
}
