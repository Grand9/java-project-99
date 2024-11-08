package hexlet.code.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
public class UserCreateDTO {
    private JsonNullable<String> firstName;
    private JsonNullable<String> lastName;

    @Email
    private String email;

    @NotBlank
    @Size(min = 3)
    private String password;
}
