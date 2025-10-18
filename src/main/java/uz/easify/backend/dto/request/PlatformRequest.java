package uz.easify.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.easify.backend.domain.enums.PlatformType;

/**
 * Request DTO for creating and updating platforms.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformRequest {

    @NotNull(message = "Platform type is required")
    private PlatformType platformType;

    @NotBlank(message = "Platform name is required")
    @Size(min = 2, max = 100, message = "Platform name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Configuration cannot exceed 500 characters")
    private String configuration;

    private Boolean enabled;

    @Size(max = 500, message = "Webhook URL cannot exceed 500 characters")
    private String webhookUrl;

    @Size(max = 255, message = "API key cannot exceed 255 characters")
    private String apiKey;
}
