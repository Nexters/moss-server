package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.SentPieceOfCake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PieceOfCakeSendRepository extends JpaRepository<SentPieceOfCake, Long> {
    @Query(nativeQuery=true, value="SELECT * FROM sent_piece_of_cakes p WHERE p.habit_id = :habitId AND p.user_id !=:userId ORDER BY random() LIMIT 1")
    SentPieceOfCake findRandomOneByHabit(@Param("userId") Long userId, @Param("habitId") Long habitId );
}
