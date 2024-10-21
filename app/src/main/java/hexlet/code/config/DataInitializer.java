package hexlet.code.config;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initializes the database with test data.
 * This class can be subclassed to customize initialization behavior.
 */
@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Runs the database initialization process.
     * Subclasses should ensure to call this method safely.
     */
    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("hexlet@example.com");
            admin.setPassword(passwordEncoder.encode("qwerty"));
            userRepository.save(admin);
        }
    }
}
