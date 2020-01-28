package nexters.moss.server.domain.repository;

import nexters.moss.server.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
