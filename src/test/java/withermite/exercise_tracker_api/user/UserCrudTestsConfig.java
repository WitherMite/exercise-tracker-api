package withermite.exercise_tracker_api.user;

import java.util.Map;

import withermite.exercise_tracker_api.test_util.CrudIntegrationTestsConfig;

public class UserCrudTestsConfig implements CrudIntegrationTestsConfig {
    @Override
    public String getJsonFilepath() {
        return "user/test-data.json";
    }

    @Override
    public Map<String, Class<?>> getDbRowMapToTypeMap() {
        return Map.of(
                "id", Integer.class,
                "username", String.class,
                "pw_hash", String.class,
                "displayname", String.class,
                "user_role", String.class,
                "are_workouts_public", Boolean.class,
                "weight", Double.class);
    }
}
