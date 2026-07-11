package withermite.exercise_tracker_api.exercise;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.AsDelta;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.Full;
import withermite.exercise_tracker_api._util.validation.constraints.NotBlankIfExists;
import withermite.exercise_tracker_api.exercise_type.ExerciseType;

public class Exercise implements Entity {
    @NotBlank(message = "Name must not be blank", groups = Full.class)
    @NotBlankIfExists(message = "Name must not be blank", groups = AsDelta.class)
    public String name;
    public String description;
    public ExerciseType exerciseType;

    @Override
    public Map<String, String> fetchUriKeys() {
        return Map.of("key", name, "exercise-type-name", exerciseType.name);
    }

}
