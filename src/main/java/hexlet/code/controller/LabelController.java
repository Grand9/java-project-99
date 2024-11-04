package hexlet.code.controller;

import hexlet.code.model.Label;
import hexlet.code.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing labels.
 * This class is designed for extension.
 * To safely extend this class, please override the methods with caution.
 */
@RestController
@RequestMapping("/api/labels")
public class LabelController {
    @Autowired
    private LabelService labelService;

    /**
     * Retrieves a label by its ID.
     *
     * @param id the ID of the label to retrieve
     * @return the label with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Label> getLabelById(@PathVariable Long id) {
        return ResponseEntity.ok(labelService.getLabelById(id));
    }

    /**
     * Retrieves all labels.
     *
     * @return a list of all labels
     */
    @GetMapping
    public ResponseEntity<List<Label>> getAllLabels() {
        return ResponseEntity.ok(labelService.getAllLabels());
    }

    /**
     * Creates a new label.
     *
     * @param label the label to create
     * @return the created label
     */
    @PostMapping
    public ResponseEntity<Label> createLabel(@RequestBody Label label) {
        return ResponseEntity.ok(labelService.createLabel(label));
    }

    /**
     * Updates an existing label.
     *
     * @param id the ID of the label to update
     * @param labelDetails the new details of the label
     * @return the updated label
     */
    @PutMapping("/{id}")
    public ResponseEntity<Label> updateLabel(@PathVariable Long id, @RequestBody Label labelDetails) {
        return ResponseEntity.ok(labelService.updateLabel(id, labelDetails));
    }

    /**
     * Deletes a label by its ID.
     *
     * @param id the ID of the label to delete
     * @return a ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }
}
