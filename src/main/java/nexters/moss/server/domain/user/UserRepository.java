package nexters.moss.server.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsBySocialId(Long socialId);
    Optional<User> findBySocialId(Long socialId);
}
