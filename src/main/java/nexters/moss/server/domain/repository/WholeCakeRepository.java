package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.Category;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.model.WholeCake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WholeCakeRepository extends JpaRepository<WholeCake, Long> {
    List<WholeCake> findAllByUserAndCategory(User user, Category category);
    int countAllByUserAndCategory(User user, Category category);
}
