package uz.easify.backend.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import uz.easify.backend.domain.enums.PlatformType;

/**
 * Platform entity representing messaging platforms integrated with the AI agent.
 * Supports Telegram, Instagram, WhatsApp, and other messaging services.
 */
@Entity
@Table(name = "platforms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Platform extends BaseEntity {

    @NotNull(message = "Platform type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "platform_type", nullable = false, unique = true, length = 50)
    private PlatformType platformType;

    @NotBlank(message = "Platform name is required")
    @Size(min = 2, max = 100, message = "Platform name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 500, message = "Configuration cannot exceed 500 characters")
    @Column(name = "configuration", length = 500)
    private String configuration;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = false;

    @Size(max = 500, message = "Webhook URL cannot exceed 500 characters")
    @Column(name = "webhook_url", length = 500)
    private String webhookUrl;

    @Size(max = 255, message = "API key cannot exceed 255 characters")
    @Column(name = "api_key", length = 255)
    private String apiKey;
}
