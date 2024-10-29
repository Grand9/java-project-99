package hexlet.code.config;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * Initializes the database with test data.
 */
@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           TaskStatusRepository taskStatusRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Runs the database initialization process.
     */
    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("hexlet@example.com");
            admin.setPassword(passwordEncoder.encode("qwerty"));
            userRepository.save(admin);
        }

        List<TaskStatus> defaultStatuses = Arrays.asList(
                new TaskStatus("New", "new"),
                new TaskStatus("In Progress", "in_progress"),
                new TaskStatus("Testing", "testing"),
                new TaskStatus("Completed", "completed")
        );

        for (TaskStatus status : defaultStatuses) {
            if (taskStatusRepository.findBySlug(status.getSlug()) == null) {
                taskStatusRepository.save(status);
            }
        }
    }
}
