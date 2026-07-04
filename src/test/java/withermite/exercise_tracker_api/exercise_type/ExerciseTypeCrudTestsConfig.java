package withermite.exercise_tracker_api.exercise_type;

import java.util.Map;

import withermite.exercise_tracker_api.test_util.CrudIntegrationTestsConfig;

public class ExerciseTypeCrudTestsConfig implements CrudIntegrationTestsConfig {

    @Override
    public String getJsonFilepath() {
        return "exercise_type/test-data.json";
    }

    @Override
    public Map<String, Class<?>> getDbRowMapToTypeMap() {
        return Map.of(
                "id", Integer.class,
                "exercise_type_name", String.class,
                "count_type", String.class,
                "load_type", String.class,
                "work_time_type", String.class,
                "rest_type", String.class);
    }
}
