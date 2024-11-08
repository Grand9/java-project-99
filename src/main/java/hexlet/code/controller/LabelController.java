package hexlet.code.controller;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/labels")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @GetMapping
    public ResponseEntity<List<LabelDTO>> getAll() {
        List<LabelDTO> labels = labelService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(labels);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<LabelDTO> getById(@PathVariable Long id) {
        LabelDTO label = labelService.show(id);
        return ResponseEntity.ok(label);
    }

    @PostMapping
    public ResponseEntity<LabelDTO> create(@Valid @RequestBody LabelCreateDTO data) {
        LabelDTO label = labelService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(label);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<LabelDTO> update(@Valid @RequestBody LabelUpdateDTO data, @PathVariable Long id) {
        LabelDTO label = labelService.update(data, id);
        return ResponseEntity.ok(label);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        labelService.destroy(id);
        return ResponseEntity.noContent().build();
    }
}
