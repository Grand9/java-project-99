package hexlet.code.component;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.CustomUserDetailsService;
import hexlet.code.service.LabelService;
import hexlet.code.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {


    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final TaskStatusService taskStatusService;
    private final LabelRepository labelRepository;
    private final LabelService labelService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createUserIfNotExists();

        createDefaultTaskStatuses();
        createDefaultLabels();
    }

    private void createUserIfNotExists() {
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            var userData = new User();
            userData.setEmail("hexlet@example.com");
            userData.setPassword("qwerty");
            customUserDetailsService.createUser(userData);
        }
    }

    private void createDefaultTaskStatuses() {
        var defaultStatuses = new ArrayList<TaskStatusCreateDTO>();
        defaultStatuses.add(new TaskStatusCreateDTO("Draft", "draft"));
        defaultStatuses.add(new TaskStatusCreateDTO("toReview", "to_review"));
        defaultStatuses.add(new TaskStatusCreateDTO("toBeFixed", "to_be_fixed"));
        defaultStatuses.add(new TaskStatusCreateDTO("toPublish", "to_publish"));
        defaultStatuses.add(new TaskStatusCreateDTO("Published", "published"));

        var currentStatuses = taskStatusRepository.findAll().stream().map(TaskStatus::getSlug).toList();

        for (var status : defaultStatuses) {
            if (!currentStatuses.contains(status.getSlug())) {
                taskStatusService.create(status);
            }
        }
    }

    private void createDefaultLabels() {
        var defaultLabels = new ArrayList<LabelCreateDTO>();

        var labelFeature = new LabelCreateDTO();
        labelFeature.setName("feature");
        defaultLabels.add(labelFeature);

        var labelBug = new LabelCreateDTO();
        labelBug.setName("bug");
        defaultLabels.add(labelBug);

        var currentLabels = labelRepository.findAll().stream().map(Label::getName).toList();

        for (var label : defaultLabels) {
            if (!currentLabels.contains(label.getName())) {
                labelService.create(label);
            }
        }
    }
}
