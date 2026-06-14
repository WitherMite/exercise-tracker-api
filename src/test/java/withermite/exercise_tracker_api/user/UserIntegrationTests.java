package withermite.exercise_tracker_api.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureRestTestClient
@Transactional
@Sql("/db/seed-schema.sql")
@Sql("/testsql/user/one-user.sql")
@Sql(scripts = "/testsql/clean.sql", executionPhase = AFTER_TEST_CLASS)
public class UserIntegrationTests {

    @Autowired
    private RestTestClient rest;

    // Crud requests
    @Test
    public void getsUserFromService() {
        String username = "frank";

        String expectedJson = """
                    {
                        "username":"frank",
                        "displayname":"Frank",
                        "role": "admin",
                        "weight": 65.2,
                        "areWorkoutsPublic": True
                    }
                """;

        rest.get().uri("/users/{username}", username)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectAll(
                        r -> r.expectStatus().isOk(),
                        r -> r.expectBody().json(expectedJson));
    }

    @Disabled("not ready for testing pagination")
    @Test
    public void getsManyUsersFromService() {
        String expectedJson = """
                    todo
                """;

        rest.get().uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectAll(
                        r -> r.expectStatus().isOk(),
                        r -> r.expectBody().json(expectedJson));
    }

    @Test
    public void postsUserToService() {
        String json = """
                    {
                        "username":"bob",
                        "displayname":"Bob",
                        "weight": 65.2
                    }
                """;
        String expectedJson = """
                    {
                        "username":"bob",
                        "displayname":"Bob",
                        "role": "default",
                        "weight": 65.2,
                        "areWorkoutsPublic": False
                    }
                """;

        rest.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .exchange().expectAll(
                        r -> r.expectStatus().isCreated(),
                        r -> r.expectBody().json(expectedJson));

    }

    @Test
    public void putsUserToService() {
        String json = """
                    {
                        "username":"bob",
                        "displayname":"Bob",
                        "role": "admin",
                        "weight": 72.4,
                        "areWorkoutsPublic": True
                    }
                """;

        rest.put().uri("/users/frank")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .exchange().expectAll(
                        r -> r.expectStatus().isOk(),
                        r -> r.expectBody().json(json));

    }

    @Test
    public void patchesUserThroughService() {
        String json = "{\"displayname\":\"frank69\", \"weight\": 65.33}";
        String expectedJson = """
                    {
                        "username":"frank",
                        "displayname":"frank69",
                        "role": "admin",
                        "weight": 65.33,
                        "areWorkoutsPublic": True
                    }
                """;

        rest.patch().uri("/users/frank")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .exchange().expectAll(
                        r -> r.expectStatus().isOk(),
                        r -> r.expectBody().json(expectedJson));

    }

    @Test
    public void deletesUserThroughService() {
        String username = "frank";

        rest.delete().uri("/users/{username}", username)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

    }

    // Edge cases

    @Test
    public void putCreatesNewUserWhenServiceCantReplace() {
        String json = """
                    {
                        "username":"frank",
                        "displayname":"Frank",
                        "role": "admin",
                        "weight": 65.2,
                        "areWorkoutsPublic": True
                    }
                """;

        rest.put().uri("/users/frank")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .exchange().expectAll(
                        r -> r.expectStatus().isCreated(),
                        r -> r.expectBody().json(json));
    }
}
