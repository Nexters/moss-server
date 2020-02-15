package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRecordRepository extends JpaRepository<HabitRecord, Long> {
    void deleteAllByUserAndHabit(User user, Habit habit);
    List<HabitRecord> findAllByUserAndHabit(User user, Habit habit);
    List<HabitRecord> findAllByUser(User user);
}
