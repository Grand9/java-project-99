package hexlet.code.service;

import hexlet.code.dto.TaskParamDTO;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.specification.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskSpecification taskSpecification;

    public List<TaskDTO> getAll(TaskParamDTO params, PageRequest pageRequest) {
        var specification = taskSpecification.build(params);
        var tasks = taskRepository.findAll(specification, pageRequest);
        return tasks.stream().map(taskMapper::map).toList();
    }

    public TaskDTO show(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return taskMapper.map(task);
    }

    public TaskDTO create(TaskCreateDTO dto) {
        var task = taskMapper.map(dto);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDTO update(TaskUpdateDTO dto, Long taskId) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + taskId + " not found."));
        taskMapper.update(dto, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public void destroy(Long id) {
        taskRepository.deleteById(id);
    }
}
