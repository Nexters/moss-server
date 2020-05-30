package nexters.moss.server.domain.habit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRecordRepository extends JpaRepository<HabitRecord, Long> {
    List<HabitRecord> findAllByUserIdAndHabitId(Long userId, Long habitId);
    void deleteByUserId(Long userId);
}
