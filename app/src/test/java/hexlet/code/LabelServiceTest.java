package hexlet.code;

import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.LabelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

public class LabelServiceTest {

    @InjectMocks
    private LabelService labelService;

    @Mock
    private LabelRepository labelRepository;

    @Mock
    private TaskRepository taskRepository;

    /**
     * Sets up the test environment before each test.
     * This method initializes the mocks used in the tests.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLabel() {
        Label label = new Label();
        label.setName("feature");

        when(labelRepository.save(label)).thenReturn(label);

        Label createdLabel = labelService.createLabel(label);

        assertNotNull(createdLabel);
        assertEquals("feature", createdLabel.getName());
        verify(labelRepository, times(1)).save(label);
    }

    @Test
    public void testGetLabelById() {
        Label label = new Label();
        label.setId(1L);
        label.setName("bug");

        when(labelRepository.findById(1L)).thenReturn(Optional.of(label));

        Label foundLabel = labelService.getLabelById(1L);

        assertNotNull(foundLabel);
        assertEquals("bug", foundLabel.getName());
        verify(labelRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateLabel() {
        Label existingLabel = new Label();
        existingLabel.setId(1L);
        existingLabel.setName("bug");

        Label updatedLabel = new Label();
        updatedLabel.setName("feature");

        when(labelRepository.findById(1L)).thenReturn(Optional.of(existingLabel));
        when(labelRepository.save(existingLabel)).thenReturn(updatedLabel);

        Label result = labelService.updateLabel(1L, updatedLabel);

        assertNotNull(result);
        assertEquals("feature", result.getName());
        verify(labelRepository, times(1)).findById(1L);
        verify(labelRepository, times(1)).save(existingLabel);
    }

    @Test
    public void testDeleteLabel() {
        Label label = new Label();
        label.setId(1L);
        label.setName("bug");

        when(labelRepository.findById(1L)).thenReturn(Optional.of(label));
        when(taskRepository.findByLabels(label)).thenReturn(Collections.emptyList());

        labelService.deleteLabel(1L);

        verify(labelRepository, times(1)).findById(1L);
        verify(labelRepository, times(1)).delete(label);
    }

    @Test
    public void testGetLabelByIdNotFound() {
        when(labelRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> labelService.getLabelById(1L));

        assertEquals("Label not found", exception.getMessage());
    }
}
