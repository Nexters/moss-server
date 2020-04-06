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

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(clause = "id ASC")
    private List<HabitRecord> habitRecords = new ArrayList<>();

    @Column(name = "habit_order")
    private int order = 0;

    @Column(name = "isActivated", columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isActivated = false;

    @Column(name = "isFirstCheck", columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isFirstCheck = false;

    @Column(name = "checkCount", columnDefinition = "integer default 0")
    @Builder.Default
    private int checkCount = 0;

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

    public int compareByOrder(Habit habit) {
        return this.getOrder() - habit.getOrder();
    }

    public void onActivation() {
        this.isActivated = true;
    }

    public void onFirstCheck() {
        this.isFirstCheck = true;
    }

    public void offFirstCheck() {
        this.isFirstCheck = false;
    }

    public void count() {
        this.checkCount++;
    }

    public boolean isFirstCheck() {
        if(this.checkCount == 1) {
            return true;
        }
        return false;
    }

    public void increaseOneOrder() {
        this.order++;
    }

    public void decreaseOneOrder() {
        this.order--;
    }

    public void changeOrder(int order) {
        this.order = order;
    }
}
