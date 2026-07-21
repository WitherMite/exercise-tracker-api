package withermite.exercise_tracker_api.user;

import java.util.Map;

import org.jooq.generated.enums.UserRoleEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.AsDelta;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.Full;
import withermite.exercise_tracker_api._util.validation.constraints.IsEnumType;
import withermite.exercise_tracker_api._util.validation.constraints.NotBlankIfExists;

public class User implements Entity {
    @NotBlank(message = "Username must not be blank", groups = Full.class)
    @NotBlankIfExists(message = "Username must not be blank", groups = AsDelta.class)
    public String username;

    @NotBlank(message = "Password must be provided", groups = Full.class)
    @Size(message = "Password must be between 5 and 32 characters", min = 5, max = 32)
    private String password; // only setter is public so jackson doesnt send in responses

    @NotBlank(message = "Displayname must not be blank", groups = Full.class)
    @NotBlankIfExists(message = "Displayname must not be blank", groups = AsDelta.class)
    public String displayname;

    @Positive(message = "Weight should greater than zero")
    public Double weight;

    @IsEnumType(enumTypeClass = UserRoleEnum.class, message = "Role must be a valid user role")
    public String role;

    public Boolean areWorkoutsPublic;

    @Override
    public Map<String, String> fetchUriKeys() {
        return Map.of("key", username);
    }

    String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
