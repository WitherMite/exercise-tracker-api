package withermite.exercise_tracker_api.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest
@AutoConfigureRestTestClient
@Sql("/db/seed-schema.sql")
@Sql("/testsql/user/populate-app-user.sql")
@Sql(scripts = "/testsql/clean.sql", executionPhase = AFTER_TEST_CLASS)
public class UserIntegrationTests {

    @Autowired
    private RestTestClient rest;

    @Autowired
    private JdbcClient jdbc;

    @Nested
    public class GetTests {
        @Test
        public void getsUserFromDB() {
            String username = "frank";

            String expectedJson = """
                        {
                            "username":"frank",
                            "displayname":"Frank",
                            "role": "admin",
                            "weight": 65.2,
                            "areWorkoutsPublic": true
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, "app_user");

            rest.get().uri("/users/{username}", username)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(expectedJson));

            int rowsAfter = countRowsInTable(jdbc, "app_user");
            assertEquals(rowsBefore, rowsAfter);
        }

        @Disabled("not ready for testing pagination")
        @Test
        public void getsManyUsersFromDB() {
            String expectedJson = """
                        todo
                    """;

            int rowsBefore = countRowsInTable(jdbc, "app_user");

            rest.get().uri("/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(expectedJson));

            int rowsAfter = countRowsInTable(jdbc, "app_user");
            assertEquals(rowsBefore, rowsAfter);
        }

        // Error tests

        @Test
        public void notFoundIfUserRequestedNotInDB() {
            String username = "bob";

            int rowsBefore = countRowsInTable(jdbc, "app_user");

            rest.get().uri("/users/{username}", username)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, "app_user");
            assertEquals(rowsBefore, rowsAfter);
        }
    }

    @Nested
    public class PostTests {
        @Test
        public void postsUserToDB() {
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
                            "areWorkoutsPublic": false
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, "app_user");

            rest.post().uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isCreated(),
                            r -> r.expectBody().json(expectedJson));

            int rowsAfter = countRowsInTable(jdbc, "app_user");
            assertEquals(1, rowsAfter - rowsBefore);

            jdbc.sql("SELECT * FROM app_user WHERE username = 'bob'")
                    .query((rs) -> {
                        assertEquals("Bob", rs.getString("displayname"));
                        assertEquals("default", rs.getString("user_role"));
                        assertEquals(false, rs.getBoolean("are_workouts_public"));
                        assertEquals(65.2d, rs.getDouble("weight"));
                    });
        }

        // Error tests

        @Test
        public void badRequestIfMissingFields() {
            String json = "{\"username\": \"bob\"}";

            int rowsBefore = countRowsInTable(jdbc, "app_user");

            rest.post().uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, "app_user");
            assertEquals(rowsBefore, rowsAfter);
        }

        @Test
        public void conflictIfUsernameNotUnique() {
            String json = """
                        {
                            "username":"frank",
                            "displayname":"Frank",
                            "role": "default",
                            "weight": 70,
                            "areWorkoutsPublic": true
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, "app_user");

            rest.post().uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange()
                    .expectStatus().isEqualTo(409)
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, "app_user");
            assertEquals(rowsBefore, rowsAfter);
        }
    }

    @Nested
    public class putTests {
        // decide if nullable and default values should
        // be optional and default to null/default
        @Test
        public void putsUserToDB() {
            String json = """
                        {
                            "username":"frank",
                            "displayname":"Frank",
                            "role": "admin",
                            "weight": 65.2,
                            "areWorkoutsPublic": true
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, "app_user");

            rest.put().uri("/users/frank")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(json));

            int rowsAfter = countRowsInTable(jdbc, "app_user");
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql("SELECT * FROM app_user WHERE username = 'frank'")
                    .query((rs) -> {
                        assertEquals("Frank", rs.getString("displayname"));
                        assertEquals("admin", rs.getString("user_role"));
                        assertEquals(true, rs.getBoolean("are_workouts_public"));
                        assertEquals(65.2d, rs.getDouble("weight"));
                    });
        }

        @Test
        public void putCreatesNewUserWhenNotInDB() {
            String json = """
                        {
                            "username":"bob",
                            "displayname":"Bob",
                            "role": "admin",
                            "weight": 72.4,
                            "areWorkoutsPublic": true
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, "app_user");

            rest.put().uri("/users/bob")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isCreated(),
                            r -> r.expectBody().json(json));

            int rowsAfter = countRowsInTable(jdbc, "app_user");
            assertEquals(1, rowsAfter - rowsBefore);

            jdbc.sql("SELECT * FROM app_user WHERE username = 'bob'")
                    .query((rs) -> {
                        assertEquals("Bob", rs.getString("displayname"));
                        assertEquals("admin", rs.getString("user_role"));
                        assertEquals(true, rs.getBoolean("are_workouts_public"));
                        assertEquals(72.4d, rs.getDouble("weight"));
                    });
        }

        // Error tests

        @Test
        public void badRequestIfMissingFields() {
            String json = "{\"displayname\": \"frank69\"}";

            int rowsBefore = countRowsInTable(jdbc, "app_user");

            rest.put().uri("/users/frank")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, "app_user");
            assertEquals(rowsBefore, rowsAfter);
        }
    }

    @Nested
    public class PatchTests {
        // should test that it can update individual fields without nulling
        // or setting others default
        // should fail if no user to update
        @Test
        public void patchesUserInDB() {
            String json = "{\"displayname\":\"frank69\", \"weight\": 65.33}";
            String expectedJson = """
                        {
                            "username":"frank",
                            "displayname":"frank69",
                            "role": "admin",
                            "weight": 65.33,
                            "areWorkoutsPublic": true
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, "app_user");

            rest.patch().uri("/users/frank")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(expectedJson));

            int rowsAfter = countRowsInTable(jdbc, "app_user");
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql("SELECT * FROM app_user WHERE username = 'frank'")
                    .query((rs) -> {
                        assertEquals("frank69", rs.getString("displayname"));
                        assertEquals("admin", rs.getString("user_role"));
                        assertEquals(true, rs.getBoolean("are_workouts_public"));
                        assertEquals(65.33d, rs.getDouble("weight"));
                    });
        }
    }

    @Nested
    public class DeleteTests {
        @Test
        public void deletesUserInDB() {
            String username = "frank";

            int rowsBefore = countRowsInTable(jdbc, "app_user");

            rest.delete().uri("/users/{username}", username)
                    .exchange()
                    .expectStatus().isNoContent()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, "app_user");
            assertEquals(rowsBefore - 1, rowsAfter);
        }

        // Error tests
        @Test
        public void notFoundIfUserNotInDB() {
            String username = "bob";

            int rowsBefore = countRowsInTable(jdbc, "app_user");

            rest.delete().uri("/users/{username}", username)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, "app_user");
            assertEquals(rowsBefore, rowsAfter);
        }
    }
}
