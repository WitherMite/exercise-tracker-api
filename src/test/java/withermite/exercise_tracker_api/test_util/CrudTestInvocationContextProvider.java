package withermite.exercise_tracker_api.test_util;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ClassTemplateInvocationContext;
import org.junit.jupiter.api.extension.ClassTemplateInvocationContextProvider;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.springframework.core.io.ClassPathResource;

public class CrudTestInvocationContextProvider implements ClassTemplateInvocationContextProvider {

    @Override
    public boolean supportsClassTemplate(ExtensionContext context) {
        // is fine to do this, we manually connect templates to providers
        // with annotations anyway, and theres no reason atm to not
        // just provide these contexts to whatever asks
        return true;
    }

    // this context stream is what the class template tests runs through
    @Override
    public Stream<ClassTemplateInvocationContext> provideClassTemplateInvocationContexts(ExtensionContext context) {
        Stream.Builder<ClassTemplateInvocationContext> contexts = Stream.builder();

        // add all invocation contexts to stream
        contexts.add(makeContext());

        return contexts.build();
    }

    // maybe @TestComponents to let spring inject to this class as beans?
    // Could have config classes based on interfaces for each Crud endpoint
    // that provide their contexts through a common injected class,
    // then a config class injected here that provides a list of
    // those config classes from spring testing beans

    // for now for i'll hardcode the one to get it working anyway
    private ClassTemplateInvocationContext makeContext() {
        return new ClassTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return "User CRUD Integration Tests";
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return List.of(
                        (TestInstancePostProcessor) (Object testInstance, ExtensionContext context) -> {
                            // inject fields to only parent class
                            if (testInstance instanceof CrudIntegrationTests crudTests) {
                                ClassPathResource sql = new ClassPathResource("testsql/user/populate-app-user.sql");

                                crudTests.populateSql = sql;
                                crudTests.tableName = "app_user";
                                crudTests.keyRowName = "username";
                                crudTests.resourceUri = "/users";
                                crudTests.existingKey = "frank";
                                crudTests.newKey = "bob";
                            }
                        });
            }
        };
    }

}
