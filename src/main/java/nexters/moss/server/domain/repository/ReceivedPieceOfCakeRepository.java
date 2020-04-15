package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.ReceivedPieceOfCake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivedPieceOfCakeRepository extends JpaRepository<ReceivedPieceOfCake, Long> {
    int countAllByUserIdAndCategoryId(Long userId, Long categoryId);
}
