package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Label label;

    /**
     * Set up the test environment. This method is called before each test.
     * It initializes the label used in the tests.
     */
    @BeforeEach
    public void setup() {
        label = new Label("Test Label");
    }

    @Test
    public void testCreateLabel() throws Exception {
        mockMvc.perform(post("/api/labels")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(label)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Label"))
                .andExpect(jsonPath("$.createdAt").exists());
    }
}
