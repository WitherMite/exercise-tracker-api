package withermite.exercise_tracker_api.test_util;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ClassTemplateInvocationContext;
import org.junit.jupiter.api.extension.ClassTemplateInvocationContextProvider;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import withermite.exercise_tracker_api.integration_tests.CrudIntegrationTests;
import withermite.exercise_tracker_api.test_config.CrudTestsConfig;

public class CrudIntegrationTestContextProvider implements ClassTemplateInvocationContextProvider {
    private final CrudTestsConfig config = new CrudTestsConfig();

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
        for (CrudIntegrationTestContext ctx : config.contexts) {
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
                return ctx.displayName;
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return List.of(
                        (TestInstancePostProcessor) (Object testInstance, ExtensionContext context) -> {
                            // inject fields to only outer class, the context provider loops through all
                            // the nested classes (and maybe methods?)
                            if (testInstance instanceof CrudIntegrationTests crudTests) {
                                crudTests.populateSql = ctx.populateSql;
                                crudTests.resourceUri = ctx.resourceUri;
                                crudTests.tableName = ctx.tableName;
                                crudTests.keyRowName = ctx.keyRowName;
                                crudTests.existingKey = ctx.existingKey;
                                crudTests.newKey = ctx.newKey;
                                crudTests.testCases = ctx.testData.testCases;
                            }
                        });
            }
        };
    }

}
