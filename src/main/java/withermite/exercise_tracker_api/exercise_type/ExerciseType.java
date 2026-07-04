package withermite.exercise_tracker_api.exercise_type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;

public class ExerciseType implements Entity<String> {
    @NotBlank(message = "Name can not be blank")
    public String name;
    @NotNull(message = "Must define a countType")
    public String countType;
    @NotNull(message = "Must define a loadType")
    public String loadType;
    @NotNull(message = "Must define a workTimeType")
    public String workTimeType;
    public String restType;

    @Override
    public String fetchKeyValue() {
        return name;
    }

}
