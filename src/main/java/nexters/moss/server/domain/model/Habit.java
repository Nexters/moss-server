package nexters.moss.server.domain.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "habit")
    private List<HabitRecord> habitRecords = new ArrayList<>();

    @Column(name = "isActivated", columnDefinition = "boolean default false")
    private Boolean isActivated;

    @Column(name = "isFirstCheck", columnDefinition = "boolean default false")
    private Boolean isFirstCheck;
}
