package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRepository extends JpaRepository<Habit, Long> {

}
