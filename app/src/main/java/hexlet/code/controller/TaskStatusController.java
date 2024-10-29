package hexlet.code.controller;

import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

/**
 * Controller for managing task statuses.
 */
@RestController
@RequestMapping("/api/task_statuses")
@Validated
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    @Autowired
    public TaskStatusController(TaskStatusService taskStatusService) {
        this.taskStatusService = taskStatusService;
    }

    /**
     * Retrieves all task statuses.
     *
     * @return a list of task statuses
     */
    @GetMapping
    public List<TaskStatus> getAllTaskStatuses() {
        return taskStatusService.getAllTaskStatuses();
    }

    /**
     * Retrieves a specific task status by its ID.
     *
     * @param id the ID of the task status to retrieve
     * @return a ResponseEntity containing the task status if found, or a 404 Not Found response
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskStatus> getTaskStatus(@PathVariable Long id) {
        return taskStatusService.getTaskStatusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new task status.
     *
     * @param taskStatus the task status to create
     * @return a ResponseEntity containing the created task status and a 201 Created status
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<TaskStatus> createTaskStatus(@Valid @RequestBody TaskStatus taskStatus) {
        TaskStatus createdStatus = taskStatusService.createTaskStatus(taskStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStatus);
    }

    /**
     * Updates an existing task status.
     *
     * @param id the ID of the task status to update
     * @param taskStatus the updated task status data
     * @return a ResponseEntity containing the updated task status
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<TaskStatus> updateTaskStatus(@PathVariable Long id,
                                                       @Valid @RequestBody TaskStatus taskStatus) {
        return ResponseEntity.ok(taskStatusService.updateTaskStatus(id, taskStatus));
    }

    /**
     * Deletes a specific task status by its ID.
     *
     * @param id the ID of the task status to delete
     * @return a ResponseEntity with a 204 No Content status
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskStatus(@PathVariable Long id) {
        taskStatusService.deleteTaskStatus(id);
        return ResponseEntity.noContent().build();
    }
}
