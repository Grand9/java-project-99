package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;


@Data
public class TaskUpdateDTO {
    @Size(min = 1)
    private JsonNullable<String> title;

    private JsonNullable<Integer> index;

    private JsonNullable<String> content;

    @JsonProperty("status")
    private JsonNullable<String> slug;

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;

    private JsonNullable<List<Long>> taskLabelIds;
}
