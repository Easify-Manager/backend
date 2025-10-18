package uz.easify.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.easify.backend.domain.enums.PlatformType;

import java.time.LocalDateTime;

/**
 * Response DTO for platform information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformResponse {

    private Long id;
    private PlatformType platformType;
    private String name;
    private String configuration;
    private Boolean enabled;
    private String webhookUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
