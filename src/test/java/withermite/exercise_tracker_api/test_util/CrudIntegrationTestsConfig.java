package withermite.exercise_tracker_api.test_util;

import java.util.Map;

public interface CrudIntegrationTestsConfig {
    public String getJsonFilepath();

    public Map<String, Class<?>> getDbRowMapToTypeMap();
}
