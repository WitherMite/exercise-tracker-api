package withermite.exercise_tracker_api.exercise_type;

import java.util.Map;

import withermite.exercise_tracker_api.test_util.CrudIntegrationTestsConfig;

public class ExerciseTypeCrudTestsConfig implements CrudIntegrationTestsConfig {

    @Override
    public String getJsonFilepath() {
        return "exercise_type/test-data.json";
    }

    @Override
    public Map<String, DBRowType> getJsonPropToDbRowMap() {
        return Map.of(
                "name",
                new DBRowType("exercise_type_name", String.class),
                "countType",
                new DBRowType("count_type", String.class),
                "loadType",
                new DBRowType("load_type", String.class),
                "workTimeType",
                new DBRowType("work_time_type", String.class),
                "restType",
                new DBRowType("rest_type", String.class));
    }
}
