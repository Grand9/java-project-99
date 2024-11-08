package hexlet.code.dto.taskStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusCreateDTO {
    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    @Size(min = 1)
    private String slug;
}
