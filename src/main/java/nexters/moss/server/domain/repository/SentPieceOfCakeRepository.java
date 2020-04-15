package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.SentPieceOfCake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SentPieceOfCakeRepository extends JpaRepository<SentPieceOfCake, Long> {
    @Query(nativeQuery=true, value="SELECT * FROM sent_piece_of_cakes p WHERE p.category_id = :categoryId AND p.user_id !=:userId ORDER BY random() LIMIT 1")
    Optional<SentPieceOfCake> findRandomByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId );
}
