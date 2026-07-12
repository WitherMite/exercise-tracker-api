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
                "id", Long.class,
                "user_id", Long.class,
                "exercise_id", Long.class,
                "datetime", Instant.class,
                "workout_count", Integer.class,
                "subjective_effort_type", String.class,
                "notes", String.class);
    }
}
