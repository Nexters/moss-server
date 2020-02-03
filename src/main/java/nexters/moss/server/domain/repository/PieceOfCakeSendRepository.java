package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.SentPieceOfCake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PieceOfCakeSendRepository extends JpaRepository<SentPieceOfCake, Long> {
}
