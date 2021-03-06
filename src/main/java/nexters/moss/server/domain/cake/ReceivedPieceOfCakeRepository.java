package nexters.moss.server.domain.cake;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivedPieceOfCakeRepository extends JpaRepository<ReceivedPieceOfCake, Long> {
    int countAllByUserIdAndCategoryId(Long userId, Long categoryId);
    Boolean existsByUserIdAndCategoryId(Long userId, Long categoryId);
    void deleteByUserId(Long userId);
}
