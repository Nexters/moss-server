package nexters.moss.server.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import nexters.moss.server.domain.value.HabitStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "habit_records")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HabitRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "habit_record_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Habit habit;

    @Enumerated(EnumType.STRING)
    @Column(name = "habit_status")
    private HabitStatus habitStatus;

    @Column(name = "date")
    private LocalDateTime date;

    @Builder
    public HabitRecord(
            Long userId,
            Long habitId,
            HabitStatus habitStatus,
            LocalDateTime date
    ) {
        this.user = User.builder()
                .id(userId)
                .build();
        this.habit = Habit.builder()
                .id(habitId)
                .build();
        this.habitStatus = habitStatus;
        this.date = date;
    }
}
