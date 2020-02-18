package nexters.moss.server.domain.model;

import lombok.*;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "habits")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Habit extends TimeProvider {
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
    @OrderBy(clause = "id ASC")
    private List<HabitRecord> habitRecords = new ArrayList<>();

    @Column(name = "isActivated", columnDefinition = "boolean default false")
    private Boolean isActivated = false;

    @Column(name = "isFirstCheck", columnDefinition = "boolean default false")
    private Boolean isFirstCheck = false;

    public Habit(
            Long categoryId,
            Long userId
    ) {
        this.category = Category.builder()
                .id(categoryId)
                .build();
        this.user = User.builder()
                .id(userId)
                .build();
    }

    public void onActivation() {
        this.isActivated = true;
    }

    public void onFirstCheck() {
        this.isFirstCheck = true;
    }
}
