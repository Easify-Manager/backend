package uz.easify.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.easify.backend.domain.entity.AgentConfiguration;

/**
 * Repository interface for AgentConfiguration entity operations.
 */
@Repository
public interface AgentConfigurationRepository extends JpaRepository<AgentConfiguration, Long> {

    /**
     * Find all non-deleted configurations.
     */
    List<AgentConfiguration> findByDeletedFalse();

    /**
     * Find a configuration by ID excluding deleted ones.
     */
    Optional<AgentConfiguration> findByIdAndDeletedFalse(Long id);

    /**
     * Find a configuration by key.
     */
    Optional<AgentConfiguration> findByConfigKeyAndDeletedFalse(String configKey);

    /**
     * Find all active configurations.
     */
    List<AgentConfiguration> findByActiveAndDeletedFalse(Boolean active);

    /**
     * Check if a configuration key already exists.
     */
    boolean existsByConfigKeyAndDeletedFalse(String configKey);
}
