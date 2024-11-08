package hexlet.code.controller;

import hexlet.code.dto.TaskParamDTO;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

@RestController
@RequestMapping(path = "/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> getAll(TaskParamDTO params, @RequestParam(defaultValue = "1") Integer page) {
        var tasks = taskService.getAll(params, PageRequest.of(page - 1, 10));
        return  ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(tasks);
    }

    @GetMapping(path = "/{id}")
    public TaskDTO getById(@PathVariable Long id) {
        return taskService.show(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO data) {
        return taskService.create(data);
    }

    @PutMapping(path = "/{id}")
    public TaskDTO update(@Valid @RequestBody TaskUpdateDTO data, @PathVariable Long id) {
        return taskService.update(data, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.destroy(id);
    }
}
