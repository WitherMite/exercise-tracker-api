package withermite.exercise_tracker_api.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.AsDelta;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.Full;
import withermite.exercise_tracker_api._util.validation.constraints.NotBlankIfExists;

public class User implements Entity<String> {

    @NotBlank(message = "Username must not be blank", groups = Full.class)
    @NotBlankIfExists(message = "Username must not be blank", groups = AsDelta.class)
    public String username;

    @NotBlank(message = "Displayname must not be blank", groups = Full.class)
    @NotBlankIfExists(message = "Displayname must not be blank", groups = AsDelta.class)
    public String displayname;

    @Positive(message = "Weight should greater than zero")
    public Double weight;
    public String role;
    public Boolean areWorkoutsPublic;

    @Override
    public String fetchKeyValue() {
        return username;
    }
}
