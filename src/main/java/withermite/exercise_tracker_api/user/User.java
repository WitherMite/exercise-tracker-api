package withermite.exercise_tracker_api.user;

import jakarta.validation.constraints.NotBlank;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;

public class User implements Entity<String> {
    @NotBlank(message = "Username must not be blank")
    public String username;
    @NotBlank(message = "Displayname must not be blank")
    public String displayname;
    public Double weight;
    public String role;
    public Boolean areWorkoutsPublic;

    @Override
    public String fetchKeyValue() {
        return username;
    }
}
