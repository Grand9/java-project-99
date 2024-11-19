package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import hexlet.code.util.UserUtils;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@AutoConfigureMockMvc
class LabelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserUtils userUtils;

    private Label testLabel;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        if (userRepository.findByEmail("user@example.com").isEmpty()) {
            User user = new User();
            user.setEmail("user@example.com");
            user.setPassword("password");
            userRepository.save(user);
        }

        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel);
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/api/labels").with(token))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetById() throws Exception {
        mockMvc.perform(get("/api/labels/" + testLabel.getId()).with(token))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        var data = new LabelCreateDTO();
        data.setName("testName");

        var request = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isCreated());

        var label = labelRepository.findByName(data.getName()).get();

        assertNotNull(label);
        assertThat(label.getName()).isEqualTo(data.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new LabelUpdateDTO();
        data.setName(JsonNullable.of("newName"));

        var request = put("/api/labels/" + testLabel.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());

        var updatedLabel = labelRepository.findByName(data.getName().get()).get();

        assertNotNull(updatedLabel);
    }

    @Test
    public void testUpdateForbidden() throws Exception {
        token = jwt().jwt(builder -> builder.subject("user@example.com"));
        var data = new LabelUpdateDTO();
        data.setName(JsonNullable.of("newName"));

        var request = put("/api/labels/" + testLabel.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        var data = new HashMap<>(Map.of(
                "name", "ne"
        ));
        var request = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/labels/" + testLabel.getId())
                        .with(token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteForbidden() throws Exception {
        token = jwt().jwt(builder -> builder.subject("user@example.com"));

        mockMvc.perform(delete("/api/labels/" + testLabel.getId())
                        .with(token))
                .andExpect(status().isForbidden());
    }
}
