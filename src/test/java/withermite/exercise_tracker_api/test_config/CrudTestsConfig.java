package withermite.exercise_tracker_api.test_config;

import org.springframework.core.io.FileSystemResource;

import withermite.exercise_tracker_api.test_util.CrudIntegrationTestContext;

public class CrudTestsConfig {
    private final String filePrefix = "src/test/resources/";
    // list json sources and DTO classes for test data here
    private final String[] jsonSources = { "user/test-data.json" };

    public final CrudIntegrationTestContext[] contexts;

    public CrudTestsConfig() {
        this.contexts = new CrudIntegrationTestContext[jsonSources.length];

        for (int i = 0; i < jsonSources.length; i++) {
            FileSystemResource json = new FileSystemResource(filePrefix + jsonSources[i]);
            this.contexts[i] = new CrudIntegrationTestContext(json);
        }
    }
}
