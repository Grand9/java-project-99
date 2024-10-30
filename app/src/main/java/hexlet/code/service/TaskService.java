package hexlet.code.service;

import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing tasks.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Retrieves all tasks.
     *
     * @return a list of all tasks
     */
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    /**
     * Finds a task by its ID.
     *
     * @param id the ID of the task
     * @return an Optional containing the task if found, otherwise empty
     */
    @Transactional(readOnly = true)
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Creates a new task.
     *
     * @param task the task to create
     * @return the created task
     */
    @Transactional
    public Task create(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Updates an existing task.
     *
     * @param id the ID of the task to update
     * @param taskDetails the updated task details
     * @return the updated task
     */
    @Transactional
    public Task update(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setName(taskDetails.getName());
        task.setIndex(taskDetails.getIndex());
        task.setDescription(taskDetails.getDescription());
        task.setTaskStatus(taskDetails.getTaskStatus());
        task.setAssignee(taskDetails.getAssignee());
        return taskRepository.save(task);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     */
    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
