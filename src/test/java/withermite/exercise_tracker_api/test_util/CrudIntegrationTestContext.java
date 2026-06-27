package withermite.exercise_tracker_api.test_util;

import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import withermite.exercise_tracker_api.test_util.data_structures.CrudTestData;
import withermite.exercise_tracker_api.test_util.data_structures.DataGroup;

public class CrudIntegrationTestContext {
    // change to a class who's constructor takes in json and populates these fields
    // and have config class let list json classpaths for each endpoint to test
    // hard problem to solve for asserting db state is how to get the correct type
    // for fields that are vague in json like numbers
    final String displayName = "User CRUD Integration Tests";
    final String resourceUri = "/users";
    final String tableName = "app_user";
    final String keyRowName = "username";
    final String existingKey = "frank";
    final String newKey = "bob";
    final ClassPathResource populateSql = new ClassPathResource("testsql/user/populate-app-user.sql");
    final CrudTestData testData = new CrudTestData((key, data) -> {
        switch (key) {
            case ReadOneExisting -> {
                return new DataGroup("""
                                {
                                    "username":"frank",
                                    "displayname":"Frank",
                                    "role": "admin",
                                    "weight": 65.2,
                                    "areWorkoutsPublic": true
                                }
                        """);
            }
            case CreateOneUniqueMinimumFields -> {
                return new DataGroup("""
                                    {
                                        "username":"bob",
                                        "displayname":"Bob",
                                        "weight": 65.2
                                    }
                        """,
                        """
                                    {
                                        "username":"bob",
                                        "displayname":"Bob",
                                        "role": "default",
                                        "weight": 65.2,
                                        "areWorkoutsPublic": false
                                    }
                                """,
                        Map.of(
                                "displayname", "Bob",
                                "user_role", "default",
                                "are_workouts_public", false,
                                "weight", 65.2d));
            }
            default -> {
                return data;
            }
        }
    });
}
