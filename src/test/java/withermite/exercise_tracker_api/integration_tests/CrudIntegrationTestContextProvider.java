package withermite.exercise_tracker_api.integration_tests;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ClassTemplateInvocationContext;
import org.junit.jupiter.api.extension.ClassTemplateInvocationContextProvider;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.springframework.core.io.FileSystemResource;

import withermite.exercise_tracker_api.exercise.ExerciseCrudTestsConfig;
import withermite.exercise_tracker_api.exercise_type.ExerciseTypeCrudTestsConfig;
import withermite.exercise_tracker_api.test_util.CrudIntegrationTestContext;
import withermite.exercise_tracker_api.test_util.CrudIntegrationTestsConfig;
import withermite.exercise_tracker_api.user.UserCrudTestsConfig;

public class CrudIntegrationTestContextProvider implements ClassTemplateInvocationContextProvider {
    // list config sources here
    private final String filePrefix = "src/test/resources/";
    private final CrudIntegrationTestsConfig[] configSources = {
            new UserCrudTestsConfig(),
            new ExerciseTypeCrudTestsConfig(),
            new ExerciseCrudTestsConfig()
            // ,new WorkoutCrudTestsConfig()
    };

    private final CrudIntegrationTestContext[] configs;

    public CrudIntegrationTestContextProvider() {
        this.configs = new CrudIntegrationTestContext[configSources.length];

        for (int i = 0; i < configSources.length; i++) {
            var config = configSources[i];
            FileSystemResource json = new FileSystemResource(filePrefix + config.getJsonFilepath());
            this.configs[i] = new CrudIntegrationTestContext(json, config.getDbRowMapToTypeMap());
        }
    }

    @Override
    public boolean supportsClassTemplate(ExtensionContext context) {
        // is fine to do this, we manually connect templates to providers
        // with annotations anyway, and theres no reason atm to not
        // just provide these contexts to whatever asks
        return true;
    }

    // this context stream is what the class template tests run through
    @Override
    public Stream<ClassTemplateInvocationContext> provideClassTemplateInvocationContexts(ExtensionContext context) {
        Stream.Builder<ClassTemplateInvocationContext> contexts = Stream.builder();

        // add all test contexts to stream
        for (CrudIntegrationTestContext ctx : configs) {
            contexts.add(getContext(ctx));
        }

        return contexts.build();
    }

    // takes test context generated from json listed in config class
    // then populates the class template context
    private ClassTemplateInvocationContext getContext(CrudIntegrationTestContext ctx) {
        return new ClassTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return ctx.testName;
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return List.of(
                        (TestInstancePostProcessor) (Object testInstance, ExtensionContext eContext) -> {
                            // inject fields to only outer class, the context provider loops through all
                            // the nested classes (and maybe methods?)
                            if (testInstance instanceof CrudIntegrationTests crudTests) {
                                crudTests.populateSql = ctx.populateSql;
                                crudTests.resourceUri = ctx.resourceUri;
                                crudTests.tableName = ctx.tableName;
                                crudTests.keyRowName = ctx.keyRowName;
                                crudTests.existingKey = ctx.existingKey;
                                crudTests.uniqueKey = ctx.newKey;
                                crudTests.testData = ctx.testData;
                            }
                        });
            }
        };
    }
}
