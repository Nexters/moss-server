package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.HabitRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRecordRepository extends JpaRepository<HabitRecord, Long> {
}
