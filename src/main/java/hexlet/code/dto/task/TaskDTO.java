package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskDTO {
    private Long id;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    @JsonProperty("status")
    private String slug;

    private String title;

    private String content;

    private Integer index;

    private List<Long> taskLabelIds;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}
