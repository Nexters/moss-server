package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.HabitRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRecordRepository extends JpaRepository<HabitRecord, Long> {
    List<HabitRecord> findAllByUser_IdAndHabit_Id(Long userId, Long habitId);
}
