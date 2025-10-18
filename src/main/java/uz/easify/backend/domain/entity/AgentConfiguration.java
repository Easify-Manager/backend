package uz.easify.backend.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * AgentConfiguration entity for storing AI agent settings and behavior parameters.
 * Can be extended to include personality traits, response templates, etc.
 */
@Entity
@Table(name = "agent_configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentConfiguration extends BaseEntity {

    @NotBlank(message = "Configuration key is required")
    @Size(max = 100, message = "Configuration key cannot exceed 100 characters")
    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey;

    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;
}
