package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.WholeCake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WholeCakeRepository extends JpaRepository<WholeCake, Long> {
    List<WholeCake> findAllByUser_IdAndCategoryId(Long userId, Long categoryId);
    int countAllByUser_IdAndCategoryId(Long userId, Long categoryId);
}
