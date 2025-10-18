package uz.easify.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating and updating agent configurations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentConfigurationRequest {

    @NotBlank(message = "Configuration key is required")
    @Size(max = 100, message = "Configuration key cannot exceed 100 characters")
    private String configKey;

    private String configValue;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Boolean active;
}
