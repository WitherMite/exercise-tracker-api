package withermite.exercise_tracker_api.exercise_type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.AsDelta;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.Full;
import withermite.exercise_tracker_api._util.validation.constraints.NotBlankIfExists;

public class ExerciseType implements Entity<String> {

    @NotBlank(message = "Name must not be blank", groups = Full.class)
    @NotBlankIfExists(message = "Name must not be blank", groups = AsDelta.class)
    public String name;

    @NotNull(message = "Must define a countType", groups = Full.class)
    public String countType;

    @NotNull(message = "Must define a loadType", groups = Full.class)
    public String loadType;

    @NotNull(message = "Must define a workTimeType", groups = Full.class)
    public String workTimeType;

    public String restType;

    @Override
    public String fetchKeyValue() {
        return name;
    }

}
