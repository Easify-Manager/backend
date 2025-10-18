package uz.easify.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA configuration for enabling auditing.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
