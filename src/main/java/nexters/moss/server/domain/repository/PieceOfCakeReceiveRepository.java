package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.ReceivedPieceOfCake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PieceOfCakeReceiveRepository extends JpaRepository<ReceivedPieceOfCake, Long> {
    int countAllByUser_IdAndCategoryId(Long userId, Long categoryId);
}
