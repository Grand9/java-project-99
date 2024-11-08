package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskStatusRepository statusRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    private User testUser;

    private TaskStatus testTaskStatus;

    private Task testTask;

    private Label testLabel;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setup() {
        token = jwt().jwt(builder -> builder.subject("aaa@bbb.com"));

        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        testTaskStatus = Instancio.of(modelGenerator.getStatusModel()).create();
        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();

        userRepository.save(testUser);
        taskStatusRepository.save(testTaskStatus);
        labelRepository.save(testLabel);

        testTask.setAssignee(testUser);
        testTask.setTaskStatus(testTaskStatus);
        testTask.setLabels(new ArrayList<>(List.of(testLabel)));

        taskRepository.save(testTask);
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/api/tasks").with(token))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetById() throws Exception {
        mockMvc.perform(get("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        var taskStatus = taskStatusRepository.findBySlug("draft").get();
        var data = new TaskCreateDTO();
        String name = "New Task Name";
        data.setTitle(name);
        data.setSlug(taskStatus.getSlug());

        var request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isCreated());
        var task = taskRepository.findByName(data.getTitle()).get();

        assertNotNull(task);
        assertThat(task.getName()).isEqualTo(data.getTitle());
        assertThat(task.getTaskStatus().getSlug()).isEqualTo(data.getSlug());
    }

   // @Test
   // public void testIndexTaskTest() throws Exception {
        //восстановление очередности проблемного автотеста хекслета
        /*2024-11-06T08:53:35.550Z DEBUG 185 --- [    Test worker] o.s.security.web.FilterChainProxy
        : Securing POST /api/task_statuses
        app-1  |     2024-11-06T08:53:35.551Z DEBUG 185 --- [    Test worker] o.s.security.web.FilterChainProxy
        : Secured POST /api/task_statuses
        app-1  |     Hibernate: insert into task_statuses (created_at,name,slug,id) values (?,?,?,default)
        app-1  |     2024-11-06T08:53:35.556Z DEBUG 185 --- [    Test worker] o.s.security.web.FilterChainProxy
        : Securing POST /api/labels
        app-1  |     2024-11-06T08:53:35.556Z DEBUG 185 --- [    Test worker] o.s.security.web.FilterChainProxy
        : Secured POST /api/labels
        app-1  |     Hibernate: insert into labels (created_at,name,id) values (?,?,default)
        app-1  |     2024-11-06T08:53:35.559Z DEBUG 185 --- [    Test worker] o.s.security.web.FilterChainProxy
        : Securing GET /api/labels
        app-1  |     2024-11-06T08:53:35.560Z DEBUG 185 --- [    Test worker] o.s.security.web.FilterChainProxy
        : Secured GET /api/labels
        app-1  |     Hibernate: select l1_0.id,l1_0.created_at,l1_0.name from labels l1_0
        app-1  |     2024-11-06T08:53:35.569Z DEBUG 185 --- [    Test worker] o.s.security.web.FilterChainProxy
        : Securing POST /api/tasks
        app-1  |     2024-11-06T08:53:35.570Z DEBUG 185 --- [    Test worker] o.s.security.web.FilterChainProxy
        : Secured POST /api/tasks
        apHibernate: select ts1_0.id,ts1_0.created_at,ts1_0.name,ts1_0.slug from task_statuses ts1_0 where ts1_0.slug=?
        app-1  |     Hibernate: select l1_0.id,l1_0.created_at,l1_0.name from labels l1_0 where l1_0.id in (?)*/

/*
        //Hibernate: insert into task_statuses (created_at,name,slug,id) values (?,?,?,default)
        //: Secured POST /api/task_statuses
        var status = new TaskStatusCreateDTO("new Status", "newStatus");
        var requestStatusCreate = post("/api/task_statuses")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(status));
        mockMvc.perform(requestStatusCreate).andExpect(status().isCreated());

        //Hibernate: insert into labels (created_at,name,id) values (?,?,default)    : Secured POST /api/labels
        var label = new LabelCreateDTO();
        label.setName("new label name");
        var requestLabelCreate = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(label));
        mockMvc.perform(requestLabelCreate).andExpect(status().isCreated());

        //Hibernate: select l1_0.id,l1_0.created_at,l1_0.name from labels l1_0    :Secured GET /api/labels
        var countLabelsRepo = labelRepository.count();
        var requestLabelAll = get("/api/labels").with(token);
        var responseResultLabels = mockMvc.perform(requestLabelAll)
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThatJson(responseResultLabels).isArray();

        List<LabelDTO> listLabelsDTO = om.readValue(responseResultLabels, new TypeReference<List<LabelDTO>>() { });
        assertThat(listLabelsDTO.size()).isEqualTo(countLabelsRepo);

        var insertedStatus = statusRepository.findBySlug(status.getSlug()).get();
        //var insertedLabel = labelRepository.findByIdIn(listLabelsDTO.).get();

    }*/

    @Test
    public void testUpdate() throws Exception {
        var updatedData = new TaskUpdateDTO();
        updatedData.setTitle(JsonNullable.of("news"));
        updatedData.setContent(JsonNullable.of("contents"));
        updatedData.setAssigneeId(JsonNullable.of(2L));

        var request = put("/api/tasks/" + testTask.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(updatedData));

        mockMvc.perform(request).andExpect(status().isOk());

        var updatedTask = taskRepository.findById(testTask.getId()).get();

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getName()).isEqualTo(updatedData.getTitle().get());
        assertThat(updatedTask.getIndex()).isEqualTo(testTask.getIndex());
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        var fakeTask = new HashMap<String, String>();
        fakeTask.put("title", "");
        fakeTask.put("slug", "");
        var request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(fakeTask));

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }



    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isNoContent());
    }
}
