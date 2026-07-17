package withermite.exercise_tracker_api.user_workout;

import java.time.Instant;
import java.util.Map;

import withermite.exercise_tracker_api.test_util.CrudIntegrationTestsConfig;

public class WorkoutCrudTestsConfig implements CrudIntegrationTestsConfig {
    @Override
    public String getJsonFilepath() {
        return "/user_workout/test-data.json";
    }

    @Override
    public Map<String, Class<?>> getDbRowMapToTypeMap() {
        return Map.of(
                "id", Integer.class,
                "user_id", Integer.class,
                "exercise_id", Integer.class,
                "datetime", Instant.class,
                "workout_count", Short.class,
                "subjective_effort_type", String.class,
                "notes", String.class);
    }
}
