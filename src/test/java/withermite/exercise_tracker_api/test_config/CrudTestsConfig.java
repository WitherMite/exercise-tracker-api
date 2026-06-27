package withermite.exercise_tracker_api.test_config;

import withermite.exercise_tracker_api.test_util.CrudIntegrationTestContext;

public class CrudTestsConfig {
    // list json sources for test data here
    // private final String[] jsonSources = {};

    public final CrudIntegrationTestContext[] contexts;

    public CrudTestsConfig() {
        // change to create all the contexts for each json source
        // could be helpful to also generate blank json template in any listed source
        // paths that arent complete
        this.contexts = new CrudIntegrationTestContext[] {
                new CrudIntegrationTestContext()
        };
    }
}
