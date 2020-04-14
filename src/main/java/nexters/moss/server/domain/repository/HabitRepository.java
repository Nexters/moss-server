package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findAllByUserId(Long userId);
    Boolean existsByUserIdAndCategory_Id(Long userId, Long categoryId);
    List<Habit> findAllByUserIdOrderByOrderAsc(Long userId);
    int countAllByUserId(Long userId);
}
