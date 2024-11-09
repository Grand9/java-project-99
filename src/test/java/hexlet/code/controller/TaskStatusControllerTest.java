package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatus.TaskStatusUpdateDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
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

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskStatusControllerTest {

    private static final String TASK_STATUSES_API_URL = "/api/task_statuses";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository repository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskStatus testTaskStatus;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setup() {
        token = jwt().jwt(builder -> builder.subject("aaa@bbb.com"));
        testTaskStatus = Instancio.of(modelGenerator.getStatusModel()).create();
        repository.save(testTaskStatus);
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(TASK_STATUSES_API_URL).with(token))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetById() throws Exception {
        mockMvc.perform(get(TASK_STATUSES_API_URL + "/" + testTaskStatus.getId()).with(token))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        var data = new TaskStatusCreateDTO("testStats", "testStats");

        var request = post(TASK_STATUSES_API_URL)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isCreated());

        var taskStatus = repository.findBySlug(data.getSlug()).orElse(null);

        assertNotNull(taskStatus);
        assertThat(taskStatus.getName()).isEqualTo(data.getName());
        assertThat(taskStatus.getSlug()).isEqualTo(data.getSlug());
    }

    @Test
    public void testUpdate() throws Exception {
        var updatedData = new TaskStatusUpdateDTO();
        updatedData.setName(JsonNullable.of("newses"));

        var request = put(TASK_STATUSES_API_URL + "/" + testTaskStatus.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedData));

        mockMvc.perform(request).andExpect(status().isOk());

        var updatedTaskStatus = repository.findById(testTaskStatus.getId()).orElse(null);

        assertNotNull(updatedTaskStatus);
        assertThat(updatedTaskStatus.getName()).isEqualTo(updatedData.getName().get());
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        var invalidData = new HashMap<String, String>();
        invalidData.put("name", "");
        invalidData.put("slug", "");

        var request = post(TASK_STATUSES_API_URL)
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidData));

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(TASK_STATUSES_API_URL + "/" + testTaskStatus.getId()).with(token))
                .andExpect(status().isNoContent());

        assertThat(repository.existsById(testTaskStatus.getId())).isFalse();
    }
}
