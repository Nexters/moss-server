package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.Category;
import nexters.moss.server.domain.model.ReceivedPieceOfCake;
import nexters.moss.server.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PieceOfCakeReceiveRepository extends JpaRepository<ReceivedPieceOfCake, Long> {
    int countAllByUserAndCategory(User user, Category category);
}
