package nexters.moss.server.domain.model;

import lombok.*;
import nexters.moss.server.domain.value.HabitStatus;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "habits",
        indexes = {
                @Index(name = "habit_user_id", columnList = "user_id")
        })
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Habit extends TimeProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "habit_id")
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(clause = "id ASC")
    @JoinColumn(name = "habit_id")
    private List<HabitRecord> habitRecords = new ArrayList<>();

    @Column(name = "habit_order")
    private Integer order = 0;

    @Column(name = "isActivated", columnDefinition = "boolean default false")
    private Boolean isActivated = false;

    @Column(name = "isFirstCheck", columnDefinition = "boolean default false")
    private Boolean isFirstCheck = false;

    @Column(name = "checkCount", columnDefinition = "integer default 0")
    private int checkCount = 0;

    public Habit(Long id) {
        this.id = id;
    }

    @Builder
    public Habit(Long categoryId, Long userId, Integer order) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.order = order;
        createHabitRecords(userId);
    }

    public void createHabitRecords(Long userId) {
        LocalDateTime date = LocalDate.now().minusDays(1).atTime(0, 0, 0);
        List<HabitRecord> habitRecords = new ArrayList<>();
        for (int day = 0; day < 5; day++) {
            HabitStatus habitStatus = HabitStatus.NOT_DONE;
            // 당일을 기점으로 3일 뒤에는 cake를 받을 수 있음.
            if (day == 3) {
                habitStatus = HabitStatus.CAKE_NOT_DONE;
            }
            habitRecords.add(
                    HabitRecord.builder()
                            .userId(userId)
                            .habitId(this.id)
                            .habitStatus(habitStatus)
                            .date(date.plusDays(day))
                            .build()
            );
        }
        this.habitRecords = habitRecords;
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

    public boolean isReadyToReceiveCake() {
        HabitRecord todayRecord = this.habitRecords.get(1);
        return todayRecord.isCakeDoneRecord();
    }

    public boolean isDone() {
        HabitRecord todayRecord = this.habitRecords.get(1);
        return todayRecord.isDone();
    }

    public void done() {
        HabitRecord todayRecord = this.habitRecords.get(1);
        switch (todayRecord.getHabitStatus()) {
            case NOT_DONE:
                todayRecord.switchHabitStatus(HabitStatus.DONE);
                break;
            case CAKE_NOT_DONE:
                todayRecord.switchHabitStatus(HabitStatus.CAKE_DONE);
                break;
        }
    }

    public void changeHabitRecords(List<HabitRecord> habitRecords) {
        this.habitRecords = habitRecords;
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

    public boolean refreshHabitHistory() {
        if(this.habitRecords.size() != 5) {
            // TODO: throw exception
        }
        // TODO: check sorted right
        int nowDay = LocalDateTime.now().getDayOfMonth();
        int recordCurrentDay = this.habitRecords.get(1).getDate().getDayOfMonth();
        HabitStatus currentHabitStatus =this.habitRecords.get(1).getHabitStatus();

        if (nowDay != recordCurrentDay) {
            switch (currentHabitStatus) {
                case CAKE_NOT_DONE:
                case NOT_DONE:
                    resetHabitRecords(this.habitRecords);
                    break;
                case DONE:
                case CAKE_DONE:
                    refreshHabitRecords(this.habitRecords);
                    break;
            }
            return true;
        }

        return false;
    }

    private void resetHabitRecords(List<HabitRecord> habitRecords) {
        for (int day = 0; day < 5; day++) {
            HabitStatus habitStatus = HabitStatus.NOT_DONE;
            if (day == 3) {
                habitStatus = HabitStatus.CAKE_NOT_DONE;
            }

            if (day == 4) {
                habitRecords.get(day).setDate(
                        habitRecords.get(day).getDate().plusDays(1)
                );
                habitRecords.get(day).setHabitStatus(habitStatus);
                continue;
            }

            habitRecords.get(day).setDate(
                    habitRecords.get(day + 1).getDate()
            );
            habitRecords.get(day).setHabitStatus(habitStatus);
        }
    }

    private void refreshHabitRecords(List<HabitRecord> habitRecords) {
        HabitStatus yesterdayHabitStatus = habitRecords.get(0).getHabitStatus();
        HabitRecord lastHabitRecord = habitRecords.get(4);
        for (int index = 0; index < 5; index++) {
            if (index == 4) {
                if(yesterdayHabitStatus == HabitStatus.DONE) {
                    if (lastHabitRecord.getHabitStatus() == HabitStatus.CAKE_NOT_DONE) {
                        lastHabitRecord.setHabitStatus(HabitStatus.NOT_DONE);
                    } else if (lastHabitRecord.getHabitStatus() == HabitStatus.NOT_DONE) {
                        lastHabitRecord.setHabitStatus(HabitStatus.CAKE_NOT_DONE);
                    }
                }

                habitRecords.get(index).setDate(
                        habitRecords.get(index).getDate().plusDays(1)
                );
                continue;
            }
            habitRecords.get(index).changeContents(
                    habitRecords.get(index + 1)
            );
        }
    }
}
