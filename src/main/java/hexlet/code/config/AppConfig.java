package hexlet.code.config;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

/**
 * Configuration class for application settings.
 * This class may be subclassed to provide additional configuration.
 */
@Configuration
public class AppConfig {

    /**
     * Initializes the database with default data.
     * This method is called at application startup to create an initial user.
     *
     * @param userRepository the user repository for database operations.
     * @param passwordEncoder the password encoder used for hashing passwords.
     * @param env the environment object to access the active profiles.
     * @return CommandLineRunner that initializes the database.
     */
    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder, Environment env) {
        return args -> {
            // Check if the 'dev' profile is active and the user doesn't already exist
            if (Arrays.stream(env.getActiveProfiles()).anyMatch(profile -> profile.equals("dev"))
                    && userRepository.findByEmail("hexlet@example.com").isEmpty()) {
                User adminUser = new User();
                adminUser.setEmail("hexlet@example.com");
                adminUser.setPassword(passwordEncoder.encode("qwerty"));
                userRepository.save(adminUser);
            }
        };
    }
}
