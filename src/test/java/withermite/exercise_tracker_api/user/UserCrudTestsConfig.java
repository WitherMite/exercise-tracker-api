package withermite.exercise_tracker_api.user;

import java.util.Map;

import withermite.exercise_tracker_api.test_util.CrudIntegrationTestsConfig;

public class UserCrudTestsConfig implements CrudIntegrationTestsConfig {
    @Override
    public String getJsonFilepath() {
        return "user/test-data.json";
    }

    @Override
    public Map<String, DBRowType> getJsonPropToDbRowMap() {
        return Map.of(
                "username",
                new DBRowType("username", String.class),
                "displayname",
                new DBRowType("displayname", String.class),
                "role",
                new DBRowType("user_role", String.class),
                "areWorkoutsPublic",
                new DBRowType("are_workouts_public", Boolean.class),
                "weight",
                new DBRowType("weight", Double.class));
    }
}
