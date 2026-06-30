package withermite.exercise_tracker_api.exercise;

import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api.exercise_type.ExerciseType;

public class Exercise implements Entity<Long> {
    public Long id;
    public String name;
    public String description;
    public ExerciseType exerciseType;

    @Override
    public Long getKey() {
        return id;
    }

}
