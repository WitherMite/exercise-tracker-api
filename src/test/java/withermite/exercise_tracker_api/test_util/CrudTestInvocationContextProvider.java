package withermite.exercise_tracker_api.test_util;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ClassTemplateInvocationContext;
import org.junit.jupiter.api.extension.ClassTemplateInvocationContextProvider;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.springframework.core.io.ClassPathResource;

import withermite.exercise_tracker_api.CrudIntegrationTests;

public class CrudTestInvocationContextProvider implements ClassTemplateInvocationContextProvider {

    @Override
    public boolean supportsClassTemplate(ExtensionContext context) {
        return true;
        // this didnt work
        // Optional<Class<?>> templateType = context.getTestClass();

        // if (templateType.isEmpty())
        // return false;

        // return templateType.getClass().isInstance(CrudIntegrationTests.class);
    }

    // this context stream is what the class template tests runs through
    @Override
    public Stream<ClassTemplateInvocationContext> provideClassTemplateInvocationContexts(ExtensionContext context) {
        Stream.Builder<ClassTemplateInvocationContext> contexts = Stream.builder();

        // add all invocation contexts to stream
        contexts.add(makeContext());
        contexts.add(makeContext()); // twice to check this is working

        return contexts.build();
    }

    // the invocation contexts can come from anywhere actually, maybe there is some
    // way to pull individual contexts defined elsewhere
    // for each run of the CRUD tests?

    // maybe @TestComponents to let spring inject to this class as beans?
    // dont think i like that much

    // for now for i'll hardcode the one to get it working anyway

    // am realizing the way the control flow here works feels backwards?
    // maybe there is a better way to do what i want,
    // like interfaces and an injected class
    // that asserts crud functionality
    private ClassTemplateInvocationContext makeContext() {
        return new ClassTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return "Template crud test for users, renamed. no. " + invocationIndex;
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return List.of(
                        (TestInstancePostProcessor) (Object testInstance, ExtensionContext context) -> {
                            // inject sql script to populate db
                            if (testInstance instanceof CrudIntegrationTests crudTests) {
                                ClassPathResource sql = new ClassPathResource("testsql/user/populate-app-user.sql");

                                crudTests.populateSql = sql;
                            }
                        });
            }
        };
    }

}
