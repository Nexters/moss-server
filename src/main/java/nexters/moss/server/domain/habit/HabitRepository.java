package nexters.moss.server.domain.habit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findAllByUserId(Long userId);
    Boolean existsByUserIdAndCategoryId(Long userId, Long categoryId);
    List<Habit> findAllByUserIdOrderByOrderAsc(Long userId);
    int countAllByUserId(Long userId);
    void deleteByUserId(Long userId);
}
