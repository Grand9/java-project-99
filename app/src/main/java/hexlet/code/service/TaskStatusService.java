package hexlet.code.service;

import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing task statuses.
 * This class is final to prevent extension as it is not designed for inheritance.
 */
@Service
public final class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    @Autowired
    public TaskStatusService(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    /**
     * Retrieves all task statuses.
     *
     * @return a list of task statuses
     */
    public List<TaskStatus> getAllTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    /**
     * Retrieves a task status by its ID.
     *
     * @param id the ID of the task status
     * @return an Optional containing the task status if found, or empty if not found
     */
    public Optional<TaskStatus> getTaskStatusById(Long id) {
        return taskStatusRepository.findById(id);
    }

    /**
     * Creates a new task status.
     *
     * @param taskStatus the task status to create
     * @return the created task status
     */
    public TaskStatus createTaskStatus(TaskStatus taskStatus) {
        return taskStatusRepository.save(taskStatus);
    }

    /**
     * Updates an existing task status.
     *
     * @param id         the ID of the task status to update
     * @param taskStatus the updated task status
     * @return the updated task status
     */
    public TaskStatus updateTaskStatus(Long id, TaskStatus taskStatus) {
        TaskStatus existingStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        existingStatus.setName(taskStatus.getName());
        existingStatus.setSlug(taskStatus.getSlug());

        return taskStatusRepository.save(existingStatus);
    }

    /**
     * Deletes a task status by its ID.
     *
     * @param id the ID of the task status to delete
     */
    public void deleteTaskStatus(Long id) {
        taskStatusRepository.deleteById(id);
    }
}
