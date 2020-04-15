package nexters.moss.server.application;

import nexters.moss.server.config.exception.ResourceNotFoundException;
import nexters.moss.server.domain.model.Category;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import static com.google.common.collect.MoreCollectors.onlyElement;

@Service
public class CategoryApplicationService {
    private final List<Category> categories;

    public CategoryApplicationService(@Qualifier("categories") List<Category> categories) {
        this.categories = categories;
    }

    public Category findById(Long categoryId) {
        try {
            return this.categories.stream()
                    .filter(categoryType -> categoryType.getId().equals(categoryId))
                    .collect(onlyElement());
        } catch (Exception e) {
            throw new ResourceNotFoundException(String.format("Has no matched category id - %s", categoryId));
        }
    }

    public boolean existById(Long categoryId) {
        try {
            this.categories.stream()
                    .filter(categoryType -> categoryType.getId().equals(categoryId))
                    .collect(onlyElement());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
