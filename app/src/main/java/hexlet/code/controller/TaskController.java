package hexlet.code.controller;

import hexlet.code.model.Task;
import hexlet.code.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * Controller for managing tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public final class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Gets a task by its ID.
     *
     * @param id the ID of the task
     * @return ResponseEntity containing the task or not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.findById(id)
                .map(task -> ResponseEntity.ok().body(task))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves tasks based on provided filter parameters.
     *
     * @param titleCont part of the task title
     * @param assigneeId ID of the assignee
     * @param status slug of the task status
     * @param labelId ID of the label
     * @return filtered list of tasks
     */
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(
            @RequestParam(required = false) String titleCont,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long labelId) {
        List<Task> tasks = taskService.getTasks(titleCont, assigneeId, status, labelId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Gets all tasks.
     *
     * @return a list of tasks
     */
    @GetMapping("/all")
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    /**
     * Creates a new task.
     *
     * @param task the task to create
     * @return ResponseEntity containing the created task
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.create(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * Updates an existing task.
     *
     * @param id the ID of the task to update
     * @param taskDetails the updated task details
     * @return ResponseEntity containing the updated task
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Task updatedTask = taskService.update(id, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
