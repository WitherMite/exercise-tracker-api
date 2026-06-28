package withermite.exercise_tracker_api.user;

import java.util.Map;

import withermite.exercise_tracker_api.test_util.CrudIntegrationTestsConfig;
import withermite.exercise_tracker_api.test_util.data_structures.CrudTestData.CaseType;

public class UserCrudTestsConfig implements CrudIntegrationTestsConfig {
    @Override
    public String jsonFilepath() {
        return "user/test-data.json";
    }

    // maybe change to be instructions to build this
    // from expectedJson ObjectNode (abstracting thats what its doing)
    // instead of making the entire map manually?
    @Override
    public Map<CaseType, Map<String, Object>> dbStateMap() {
        return Map.of(
                CaseType.CreateOneUniqueMinimumFields,
                Map.of( // temp hardcoded for CreateOneUniqueMinimumFields
                        "displayname", "Bob",
                        "user_role", "default",
                        "are_workouts_public", false,
                        "weight", 65.2d));
    }
}
