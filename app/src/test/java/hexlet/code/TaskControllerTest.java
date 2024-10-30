package hexlet.code;

import hexlet.code.controller.TaskController;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the TaskController.
 */
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private Task task;
    private TaskStatus taskStatus;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        taskStatus = new TaskStatus();
        taskStatus.setId(1L);
        taskStatus.setName("In Progress");

        task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setDescription("This is a test task.");
        task.setTaskStatus(taskStatus);
    }

    @Test
    public void testGetTaskById() throws Exception {
        when(taskService.findById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Task"))
                .andExpect(jsonPath("$.description").value("This is a test task."));
    }

    @Test
    public void testGetTaskByIdNotFound() throws Exception {
        when(taskService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllTasks() throws Exception {
        List<Task> tasks = List.of(task);
        when(taskService.findAll()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Task"));
    }

    @Test
    public void testCreateTask() throws Exception {
        when(taskService.create(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Task\",\"description\":\"This is a test task.\","
                                + "\"taskStatus\":{\"id\":1}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Task"));
    }

    @Test
    public void testUpdateTask() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setName("Updated Task");
        updatedTask.setDescription("Updated description");
        updatedTask.setTaskStatus(taskStatus);

        when(taskService.update(eq(1L), any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Task\",\"description\":\"Updated description\","
                                + "\"taskStatus\":{\"id\":1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    public void testDeleteTask() throws Exception {
        doNothing().when(taskService).delete(1L);

        mockMvc.perform(delete("/api/tasks/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
