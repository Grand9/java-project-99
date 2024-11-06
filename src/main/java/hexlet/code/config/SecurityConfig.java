package hexlet.code.config;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    /**
     * Configures the security filter chain.
     *
     * @param http The HttpSecurity object to be configured.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/task_statuses").authenticated()
                        .requestMatchers("/api/task_statuses/**").authenticated()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Provides a password encoder.
     *
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides a JWT authentication filter.
     *
     * @return A JwtAuthenticationFilter instance.
     * @throws IOException If there is an issue with reading the public key.
     */
    @Bean
    public Filter jwtAuthenticationFilter() throws IOException {
        PublicKey publicKey = loadPublicKey();
        return new JwtAuthenticationFilter(userDetailsService, publicKey);
    }

    /**
     * Loads the public key from the classpath.
     *
     * @return The PublicKey instance.
     * @throws IOException If there is an issue with reading the public key.
     */
    private PublicKey loadPublicKey() throws IOException {
        try {
            // Example of loading a public key from the classpath
            var publicKeyResource = new ClassPathResource("certs/public_key.pem");
            byte[] keyBytes = publicKeyResource.getInputStream().readAllBytes();

            // Remove the "BEGIN PUBLIC KEY" and "END PUBLIC KEY" parts if they exist
            String keyString = new String(keyBytes);
            keyString = keyString.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\n", "");

            // Decode the base64 string into bytes
            byte[] decoded = Base64.getDecoder().decode(keyString);

            // Create the public key spec
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);

            // Generate the public key
            return java.security.KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (Exception e) {
            throw new IOException("Error loading public key", e);
        }
    }

    /**
     * Configures the authentication manager.
     *
     * @param http The HttpSecurity object.
     * @return The configured AuthenticationManager.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
