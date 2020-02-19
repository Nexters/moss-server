package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRecordRepository extends JpaRepository<HabitRecord, Long> {
    void deleteAllByUser_IdAndHabit_Id(Long userId, Long habitId);
    void deleteByUser_Id(Long userId);
    List<HabitRecord> findAllByUser_IdAndHabit_Id(Long userId, Long habitId);
    List<HabitRecord> findAllByUser_Id(Long userId);
}
