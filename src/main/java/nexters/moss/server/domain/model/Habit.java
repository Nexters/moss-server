package nexters.moss.server.domain.model;

import lombok.*;

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

    @Column(name = "habit_name")
    private String habitName;

    @Column(name = "cake_name")
    private String cakeName;

    @Column(name = "whole_cake_image_path")
    private String wholeCakeImagePath;

    @Column(name = "piece_of_cake_image_path")
    private String pieceOfCakeImagePath;
}
