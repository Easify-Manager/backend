package uz.easify.backend.service.mapper;

import org.springframework.stereotype.Component;
import uz.easify.backend.domain.entity.Category;
import uz.easify.backend.dto.response.CategoryResponse;

/**
 * Mapper class for converting Category entities to DTOs.
 */
@Component
public class CategoryMapper {

    /**
     * Convert Category entity to CategoryResponse DTO.
     */
    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .displayOrder(category.getDisplayOrder())
                .active(category.getActive())
                .productCount(category.getProducts() != null ? 
                        (int) category.getProducts().stream().filter(p -> !p.getDeleted()).count() : 0)
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
