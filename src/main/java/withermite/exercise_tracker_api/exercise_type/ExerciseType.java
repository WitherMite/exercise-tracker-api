package withermite.exercise_tracker_api.exercise_type;

import withermite.exercise_tracker_api._util.crud_behaviors.Entity;

public class ExerciseType implements Entity<String> {
    public String name;
    public String countType;
    public String loadType;
    public String workTimeType;
    public String restType;

    @Override
    public String fetchKeyValue() {
        return name;
    }

}
