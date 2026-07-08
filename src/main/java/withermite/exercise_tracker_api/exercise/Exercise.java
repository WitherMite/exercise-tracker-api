package withermite.exercise_tracker_api.exercise;

import jakarta.validation.constraints.Positive;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api._util.validation.constraints.NotBlankIfExists;
import withermite.exercise_tracker_api.exercise_type.ExerciseType;

public class Exercise implements Entity<Long> {
    @Positive
    public Long id;
    @NotBlankIfExists
    public String name;
    @NotBlankIfExists
    public String description;
    public ExerciseType exerciseType;

    @Override
    public Long fetchKeyValue() {
        return id;
    }

}
