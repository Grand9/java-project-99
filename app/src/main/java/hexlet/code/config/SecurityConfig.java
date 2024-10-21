package hexlet.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application.
 * This class can be subclassed to customize security settings.
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures security settings for the application.
     *
     * @param http the HttpSecurity object to configure security settings.
     * @return SecurityFilterChain configured security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/users").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }

    /**
     * Provides a PasswordEncoder bean for encoding passwords.
     * <p>
     * This method can be safely overridden by subclasses to provide
     * a different PasswordEncoder implementation.
     *
     * @return PasswordEncoder bean used for password encoding.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
