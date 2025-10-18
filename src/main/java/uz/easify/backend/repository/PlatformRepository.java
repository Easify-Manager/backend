package uz.easify.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.easify.backend.domain.entity.Platform;
import uz.easify.backend.domain.enums.PlatformType;

/**
 * Repository interface for Platform entity operations.
 */
@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {

    /**
     * Find all non-deleted platforms.
     */
    List<Platform> findByDeletedFalse();

    /**
     * Find a platform by ID excluding deleted ones.
     */
    Optional<Platform> findByIdAndDeletedFalse(Long id);

    /**
     * Find a platform by type.
     */
    Optional<Platform> findByPlatformTypeAndDeletedFalse(PlatformType platformType);

    /**
     * Find all enabled platforms.
     */
    List<Platform> findByEnabledAndDeletedFalse(Boolean enabled);

    /**
     * Check if a platform type already exists.
     */
    boolean existsByPlatformTypeAndDeletedFalse(PlatformType platformType);
}
