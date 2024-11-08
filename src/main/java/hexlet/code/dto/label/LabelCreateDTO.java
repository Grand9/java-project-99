package hexlet.code.dto.label;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LabelCreateDTO {
    @Size(min = 3, max = 1000)
    private String name;
}
