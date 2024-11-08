package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelGenerator modelGenerator;

    private User testUser;
    private JwtRequestPostProcessor token;

    @BeforeEach
    public void setUser() {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    @Test
    public void testIndex() throws Exception {
        userRepository.save(testUser);

        var result = mockMvc.perform(get("/api/users").with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        userRepository.save(testUser);

        var request = get("/api/users/" + testUser.getId()).with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName()),
                v -> v.node("email").isEqualTo(testUser.getEmail()));
    }

    @Test
    public void testCreate() throws Exception {
        var createdUser = new UserCreateDTO();
        createdUser.setPassword("123");
        createdUser.setEmail("ccccc@ddddd.com");

        var request = post("/api/users")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdUser));

        mockMvc.perform(request).andExpect(status().isCreated());
        var user = userRepository.findByEmail(createdUser.getEmail()).get();
        assertNotNull(user);
        assertThat(user.getEmail()).isEqualTo(createdUser.getEmail());
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        var fakeUser = new HashMap<String, String>();
        fakeUser.put("email", "2");
        fakeUser.put("password", "0");
        var request = post("/api/users")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fakeUser));

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {
        userRepository.save(testUser);
        var updatedData = new UserUpdateDTO();
        updatedData.setFirstName(JsonNullable.of("UpdatedName"));

        var request = put("/api/users/" + testUser.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedData));

        mockMvc.perform(request).andExpect(status().isOk());
        var updatedUser = userRepository.findById(testUser.getId()).get();
        assertThat(updatedUser.getFirstName()).isEqualTo(updatedData.getFirstName().get());
    }

    @Test
    public void testDelete() throws Exception {
        userRepository.save(testUser);

        var request = delete("/api/users/" + testUser.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(userRepository.existsById(testUser.getId())).isEqualTo(false);
    }

    @Test
    public void testIndexWithoutAuth() throws Exception {
        userRepository.save(testUser);
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());

    }
}
