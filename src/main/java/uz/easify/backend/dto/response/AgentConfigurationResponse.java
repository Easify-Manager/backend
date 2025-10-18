package uz.easify.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for agent configuration information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentConfigurationResponse {

    private Long id;
    private String configKey;
    private String configValue;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
