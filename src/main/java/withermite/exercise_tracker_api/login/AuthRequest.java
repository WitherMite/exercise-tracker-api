package withermite.exercise_tracker_api.login;

import java.util.Map;

import org.jooq.generated.enums.UserRoleEnum;

import jakarta.validation.constraints.NotBlank;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api._util.validation.constraints.IsEnumType;

public class AuthRequest implements Entity {
    @NotBlank(message = "Username must not be blank")
    public String username;

    @NotBlank(message = "Password must not be blank")
    public String password;

    @IsEnumType(enumTypeClass = UserRoleEnum.class, message = "Role must be a valid user role")
    public String role;

    @Override
    public Map<String, String> fetchUriKeys() {
        return Map.of("key", username);
    }
}
