package hexlet.code.dto.taskStatus;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
public class TaskStatusUpdateDTO {
    @Column(unique = true)
    @Size(min = 1)
    private JsonNullable<String> name;

    @Column(unique = true)
    @Size(min = 1)
    private JsonNullable<String> slug;
}
