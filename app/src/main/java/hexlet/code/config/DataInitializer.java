package hexlet.code.config;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * Initializes the application data on startup.
 * This class is designed for extension. If you plan to subclass it, please ensure
 * that you override the 'run' method properly to avoid breaking the initialization logic.
 */
@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           TaskStatusRepository taskStatusRepository,
                           LabelRepository labelRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.labelRepository = labelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Runs the data initialization logic.
     * <p>
     * This method creates a default admin user, default task statuses, and default labels
     * if they do not already exist in the repository.
     * </p>
     *
     * @param args command line arguments
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
            if (taskStatusRepository.findBySlug(status.getSlug()).isEmpty()) {
                taskStatusRepository.save(status);
            }
        }

        List<Label> defaultLabels = Arrays.asList(
                new Label("feature"),
                new Label("bug")
        );

        for (Label label : defaultLabels) {
            if (labelRepository.findByName(label.getName()).isEmpty()) {
                labelRepository.save(label);
            }
        }
    }
}
