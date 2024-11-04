package hexlet.code.service;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing labels.
 * This class is designed for extension.
 * To safely extend this class, please override the methods with caution.
 */
@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Retrieves all labels.
     *
     * @return a list of all labels
     */
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    /**
     * Retrieves a label by its ID.
     *
     * @param id the ID of the label to retrieve
     * @return the label with the specified ID
     */
    public Label getLabelById(Long id) {
        return labelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Label not found"));
    }

    /**
     * Creates a new label.
     *
     * @param label the label to create
     * @return the created label
     */
    public Label createLabel(Label label) {
        label.setCreatedAt(LocalDateTime.now());
        return labelRepository.save(label);
    }

    /**
     * Updates an existing label.
     *
     * @param id the ID of the label to update
     * @param labelDetails the new details of the label
     * @return the updated label
     */
    public Label updateLabel(Long id, Label labelDetails) {
        Label label = getLabelById(id);
        label.setName(labelDetails.getName());
        return labelRepository.save(label);
    }

    /**
     * Deletes a label by its ID.
     *
     * @param id the ID of the label to delete
     * @throws IllegalStateException if the label is associated with tasks
     */
    public void deleteLabel(Long id) {
        Label label = getLabelById(id);
        if (labelHasTasks(label)) {
            throw new IllegalStateException("Cannot delete label that is associated with tasks");
        }
        labelRepository.delete(label);
    }

    private boolean labelHasTasks(Label label) {
        return !taskRepository.findByLabels(label).isEmpty();
    }
}
