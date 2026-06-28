package withermite.exercise_tracker_api.test_util;

import java.util.Map;

import withermite.exercise_tracker_api.test_util.data_structures.CrudTestData.CaseType;

public interface CrudIntegrationTestsConfig {
    public String jsonFilepath();

    public Map<CaseType, Map<String, Object>> dbStateMap();
}
