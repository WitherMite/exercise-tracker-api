package withermite.exercise_tracker_api.test_util;

import java.util.Map;

public interface CrudIntegrationTestsConfig {
    public record DBRowType(String rowName, Class<?> type) {
    }

    public String getJsonFilepath();

    public Map<String, DBRowType> getJsonPropToDbRowMap();
}
