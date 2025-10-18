package uz.easify.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.easify.backend.domain.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity operations.
 * Provides methods for managing product categories with hierarchy support.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find all non-deleted categories.
     */
    List<Category> findByDeletedFalse();

    /**
     * Find all active, non-deleted categories.
     */
    List<Category> findByActiveAndDeletedFalse(Boolean active);

    /**
     * Find a category by ID excluding deleted ones.
     */
    Optional<Category> findByIdAndDeletedFalse(Long id);

    /**
     * Find top-level categories (no parent).
     */
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.deleted = false ORDER BY c.displayOrder, c.name")
    List<Category> findTopLevelCategories();

    /**
     * Find child categories of a parent.
     */
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId AND c.deleted = false ORDER BY c.displayOrder, c.name")
    List<Category> findByParentId(Long parentId);

    /**
     * Check if a category name already exists.
     */
    boolean existsByNameAndDeletedFalse(String name);

    /**
     * Count products in a category.
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId AND p.deleted = false")
    Long countProductsInCategory(Long categoryId);
}
