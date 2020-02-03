package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
