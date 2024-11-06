package hexlet.code;

import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("dev")
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    /**
     * Sets up the test environment for each test case.
     * This method initializes mocks and any other necessary setup
     * for the tests. If you need to extend this class, ensure
     * to call super.setUp() to maintain the setup behavior.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFilterTasksByTitle() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("Create new version");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setName("Update old version");

        when(taskRepository.findTasksByFilters("Create", null, null, null))
                .thenReturn(List.of(task1));

        List<Task> result = taskService.getTasks("Create", null, null, null);

        assertEquals(1, result.size());
        assertEquals("Create new version", result.getFirst().getName());
    }

    @Test
    public void testFilterTasksByAssignee() {
        Task task = new Task();
        task.setId(1L);
        task.setAssignee(new User(1L));

        when(taskRepository.findTasksByFilters(null, 1L, null, null))
                .thenReturn(List.of(task));

        List<Task> result = taskService.getTasks(null, 1L, null, null);

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getAssignee().getId());
    }

    @Test
    public void testFilterTasksByStatus() {
        Task task = new Task();
        task.setId(1L);
        task.setStatus(new TaskStatus("to_be_fixed"));

        when(taskRepository.findTasksByFilters(null, null, "to_be_fixed", null))
                .thenReturn(List.of(task));

        List<Task> result = taskService.getTasks(null, null, "to_be_fixed", null);

        assertEquals(1, result.size());
        assertEquals("to_be_fixed", result.getFirst().getStatus().getSlug());
    }

    @Test
    public void testFilterTasksByLabel() {
        Label label = new Label();
        label.setId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setLabels(Set.of(label));

        when(taskRepository.findTasksByFilters(null, null, null, 1L))
                .thenReturn(List.of(task));

        List<Task> result = taskService.getTasks(null, null, null, 1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getLabels().iterator().next().getId());
    }
}
