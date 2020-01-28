package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.WholeCake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WholeCakeRepository extends JpaRepository<WholeCake, String> {
}
