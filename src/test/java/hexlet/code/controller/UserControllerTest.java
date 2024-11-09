package hexlet.code.controller;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private static final String USERS_API_URL = "/api/users";
    private User testUser;
    private JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    private void saveTestUser() {
        userRepository.save(testUser);
    }

    @Test
    public void testIndex() throws Exception {
        saveTestUser();
        var result = mockMvc.perform(MockMvcRequestBuilders.get(USERS_API_URL).with(token))
                .andExpect(status().isOk())
                .andReturn();
        assertThatJson(result.getResponse().getContentAsString()).isArray();
    }

    @Test
    public void testShow() throws Exception {
        saveTestUser();
        var result = mockMvc.perform(MockMvcRequestBuilders.get(USERS_API_URL + "/" + testUser.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn();
        assertThatJson(result.getResponse().getContentAsString()).and(
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName()),
                v -> v.node("email").isEqualTo(testUser.getEmail()));
    }

    @Test
    public void testCreate() throws Exception {
        var createdUser = new UserCreateDTO();
        createdUser.setPassword("123");
        createdUser.setEmail("ccccc@ddddd.com");

        mockMvc.perform(MockMvcRequestBuilders.post(USERS_API_URL)
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser)))
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(createdUser.getEmail()).orElse(null);
        assertNotNull(user);
        assertThat(user.getEmail()).isEqualTo(createdUser.getEmail());
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        var invalidUser = new HashMap<String, String>();
        invalidUser.put("email", "2");
        invalidUser.put("password", "0");

        mockMvc.perform(MockMvcRequestBuilders.post(USERS_API_URL)
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {
        saveTestUser();
        var updatedData = new UserUpdateDTO();
        updatedData.setFirstName(JsonNullable.of("UpdatedName"));

        mockMvc.perform(MockMvcRequestBuilders.put(USERS_API_URL + "/" + testUser.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isOk());

        var updatedUser = userRepository.findById(testUser.getId()).orElse(null);
        assert updatedUser != null;
        assertThat(updatedUser.getFirstName()).isEqualTo(updatedData.getFirstName().get());
    }

    @Test
    public void testDelete() throws Exception {
        saveTestUser();

        mockMvc.perform(MockMvcRequestBuilders.delete(USERS_API_URL + "/" + testUser.getId()).with(token))
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(testUser.getId())).isFalse();
    }

    @Test
    public void testIndexWithoutAuth() throws Exception {
        saveTestUser();
        mockMvc.perform(MockMvcRequestBuilders.get(USERS_API_URL)).andExpect(status().isUnauthorized());
    }
}
