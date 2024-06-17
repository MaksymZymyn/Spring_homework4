package homework4;

import homework4.domain.SysUser;
import homework4.security.AuditorAwareImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "customAuditorAware")
public class JpaAuditingConfiguration {

    @Bean
    public AuditorAware<SysUser> customAuditorAware() {
        return new AuditorAwareImpl();
    }
}
