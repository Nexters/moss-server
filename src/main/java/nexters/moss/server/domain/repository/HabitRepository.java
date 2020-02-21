package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    Boolean existsByUser_IdAndCategory_Id(Long userId, Long categoryId);
    void deleteByUser_Id(Long userId);
    List<Habit> findAllByUser_IdOrderByOrderAsc(Long userId);
    int countAllByUser_Id(Long userId);
}
