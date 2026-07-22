package withermite.exercise_tracker_api.login;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;

public class AuthRequest implements Entity {
    @NotBlank(message = "Username must not be blank")
    public String username;

    @NotBlank(message = "Password must not be blank")
    public String password;

    String role;

    @Override
    public Map<String, String> fetchUriKeys() {
        return Map.of("key", username);
    }
}
