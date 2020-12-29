package com.asconsoft.gintaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> SecurityContextHolder.getContext().getAuthentication()!=null?
                Optional.of(SecurityContextHolder.getContext().getAuthentication().getName())
                :Optional.of("Gintaa-System");
    }
}
