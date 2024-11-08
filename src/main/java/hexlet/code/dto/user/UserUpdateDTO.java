package hexlet.code.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
public class UserUpdateDTO {
    private JsonNullable<String> firstName;
    private JsonNullable<String> lastName;

    @Email
    private JsonNullable<String> email;

    @NotBlank(message = "Password must be at least 3 characters")
    @Size(min = 3)
    private JsonNullable<String> password;
}
