package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.Description;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescriptionRepository extends JpaRepository<Description, Long> {
}
