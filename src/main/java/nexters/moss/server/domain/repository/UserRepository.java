package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsBySocialId(Long socialId);
}
