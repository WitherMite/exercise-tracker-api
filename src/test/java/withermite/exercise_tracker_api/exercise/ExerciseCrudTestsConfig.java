package withermite.exercise_tracker_api.exercise;

import java.util.Map;

import withermite.exercise_tracker_api.test_util.CrudIntegrationTestsConfig;

public class ExerciseCrudTestsConfig implements CrudIntegrationTestsConfig {

    @Override
    public String getJsonFilepath() {
        return "/exercise/test-data.json";
    }

    @Override
    public Map<String, Class<?>> getDbRowMapToTypeMap() {
        return Map.of(
                "id", Long.class,
                "exercise_name", String.class,
                "exercise_type_id", Long.class,
                "description", String.class);
    }
}
