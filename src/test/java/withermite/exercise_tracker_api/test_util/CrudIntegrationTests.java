package withermite.exercise_tracker_api.test_util;

import java.net.URI;
import java.util.Map;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassTemplate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.jdbc.Sql;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import withermite.exercise_tracker_api.test_util.data_providers.CrudTestData.CaseType;
import withermite.exercise_tracker_api.test_util.data_providers.DataGroup;

@ClassTemplate
@ExtendWith(CrudTestInvocationContextProvider.class)
@SpringBootTest
@AutoConfigureRestTestClient
@Sql("/db/seed-schema.sql")
@Sql(scripts = "/testsql/clean.sql", executionPhase = AFTER_TEST_CLASS)
public class CrudIntegrationTests {

    @Autowired
    private RestTestClient rest;

    @Autowired
    private JdbcClient jdbc;

    @Autowired
    private DataSource dataSource;

    private final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    ClassPathResource populateSql;
    Map<CaseType, DataGroup> testData;
    String resourceUri;
    String tableName;
    String keyRowName;
    String existingKey;
    String newKey;

    private String sqlSelectExisting() {
        return "SELECT * FROM " + tableName + " WHERE " + keyRowName + " = '" + existingKey + "'";
    }

    private String sqlSelectNew() {
        return "SELECT * FROM " + tableName + " WHERE " + keyRowName + " = '" + newKey + "'";
    }

    @BeforeEach
    @SuppressWarnings("unused")
    void populateDB() {
        populator.addScript(populateSql);
        populator.execute(dataSource);
    }

    @Nested
    public class GetTests {
        @Test
        public void getsFromDB() {
            DataGroup data = testData.get(CaseType.ReadOneExisting);
            String expectedJson = data.expectedJson;

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.get().uri(resourceUri + "/{key}", existingKey)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(expectedJson));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }

        // Pagination tests (need to fix expecting db order with no order by clause)

        @Test
        public void getsManyFromDB() {
            // defaulting to a page size limit of 5 for now
            // extract expected json into resource file in refactor
            // to prevent thing like this
            String expectedJson = """
                        [
                            {
                                "username":"frank",
                                "displayname":"Frank",
                                "role": "admin",
                                "weight": 65.2,
                                "areWorkoutsPublic": true
                            },
                            {
                                "username":"frank2",
                                "displayname":"Frank",
                                "role": "admin",
                                "weight": 65.2,
                                "areWorkoutsPublic": true
                            },
                            {
                                "username":"frank3",
                                "displayname":"Frank",
                                "role": "admin",
                                "weight": 65.2,
                                "areWorkoutsPublic": true
                            },
                            {
                                "username":"frank4",
                                "displayname":"Frank",
                                "role": "admin",
                                "weight": 65.2,
                                "areWorkoutsPublic": true
                            },
                            {
                                "username":"frank5",
                                "displayname":"Frank",
                                "role": "admin",
                                "weight": 65.2,
                                "areWorkoutsPublic": true
                            }
                        ]
                    """;

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.get().uri(resourceUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(expectedJson));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }

        @Test
        public void getsSpecifiedCountAndOffsetFromDB() {
            String expectedJson = """
                        [
                            {
                                "username":"frank3",
                                "displayname":"Frank",
                                "role": "admin",
                                "weight": 65.2,
                                "areWorkoutsPublic": true
                            },
                            {
                                "username":"frank4",
                                "displayname":"Frank",
                                "role": "admin",
                                "weight": 65.2,
                                "areWorkoutsPublic": true
                            }
                        ]
                    """;

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.get().uri(resourceUri + "?limit=2&offset=2")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(expectedJson));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }

        // Error tests

        @Test
        public void notFoundIfRequestedNotInDB() {

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.get().uri(resourceUri + "/{key}", newKey)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }
    }

    @Nested
    public class PostTests {
        @Test
        public void postsToDB() {
            DataGroup data = testData.get(CaseType.CreateOneUniqueMinimumFields);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.post().uri(resourceUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.inputJson)
                    .exchange().expectAll(
                            r -> r.expectStatus().isCreated(),
                            r -> r.expectBody().json(data.expectedJson));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(1, rowsAfter - rowsBefore);

            jdbc.sql(sqlSelectNew())
                    .query((rs) -> {
                        data.assertDbState(rs);
                    });
        }

        // Error tests

        @Test
        public void badRequestIfMissingFields() {
            String json = "{\"username\": \"bob\"}";

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.post().uri(resourceUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }

        @Test
        public void conflictIfKeyNotUnique() {
            String json = """
                        {
                            "username":"frank",
                            "displayname":"Frank",
                            "role": "default",
                            "weight": 70,
                            "areWorkoutsPublic": true
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.post().uri(resourceUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange()
                    .expectStatus().isEqualTo(409)
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }
    }

    @Nested
    public class putTests {
        @Test
        public void putsToDB() {
            String json = """
                        {
                            "username":"frank",
                            "displayname":"Frank69",
                            "role": "default",
                            "weight": 75,
                            "areWorkoutsPublic": true
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.put().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql(sqlSelectExisting())
                    .query((rs) -> {
                        assertEquals("Frank69", rs.getString("displayname"));
                        assertEquals("default", rs.getString("user_role"));
                        assertEquals(true, rs.getBoolean("are_workouts_public"));
                        assertEquals(75d, rs.getDouble("weight"));
                    });
        }

        @Test
        public void ommitedOptionalFieldsSetDefaultValues() {
            String json = """
                        {
                            "username":"frank",
                            "displayname":"Frank"
                        }
                    """;
            String expectedJson = """
                        {
                            "username":"frank",
                            "displayname":"Frank",
                            "role": "default",
                            "weight": null,
                            "areWorkoutsPublic": false
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.put().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(expectedJson));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql(sqlSelectExisting())
                    .query((rs) -> {
                        assertEquals("Frank", rs.getString("displayname"));
                        assertEquals("default", rs.getString("user_role"));
                        assertEquals(false, rs.getBoolean("are_workouts_public"));
                        assertEquals(null, rs.getObject("weight", Double.class));
                    });
        }

        @Test
        public void putCreatesNewResourceWhenNotInDB() {
            String json = """
                        {
                            "username":"bob",
                            "displayname":"Bob",
                            "role": "admin",
                            "weight": 72.4,
                            "areWorkoutsPublic": true
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.put().uri(resourceUri + "/{key}", newKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isCreated(),
                            r -> r.expectBody().json(json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(1, rowsAfter - rowsBefore);

            jdbc.sql(sqlSelectNew())
                    .query((rs) -> {
                        assertEquals("Bob", rs.getString("displayname"));
                        assertEquals("admin", rs.getString("user_role"));
                        assertEquals(true, rs.getBoolean("are_workouts_public"));
                        assertEquals(72.4d, rs.getDouble("weight"));
                    });
        }

        @Test
        public void seeOtherStatusWhenKeyChanges() {
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(resourceUri + "/{key}").build(newKey);

            String json = """
                        {
                            "username":"bob",
                            "displayname":"Bob",
                            "role": "default",
                            "weight": 75,
                            "areWorkoutsPublic": true
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.put().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isEqualTo(303)
                                    .expectHeader().location(location.toString()),
                            r -> r.expectBody().json(json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql(sqlSelectNew())
                    .query((rs) -> {
                        assertEquals("Bob", rs.getString("displayname"));
                        assertEquals("default", rs.getString("user_role"));
                        assertEquals(true, rs.getBoolean("are_workouts_public"));
                        assertEquals(75d, rs.getDouble("weight"));
                    });

        }
        // Error tests

        @Test
        public void badRequestIfMissingFields() {
            String json = "{\"displayname\": \"frank69\"}";

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.put().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }
    }

    @Nested
    public class PatchTests {
        @Test
        public void patchesInDB() {
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

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.patch().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(expectedJson));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql(sqlSelectExisting())
                    .query((rs) -> {
                        assertEquals("frank69", rs.getString("displayname"));
                        assertEquals("admin", rs.getString("user_role"));
                        assertEquals(true, rs.getBoolean("are_workouts_public"));
                        assertEquals(65.33d, rs.getDouble("weight"));
                    });
        }

        @Test
        public void doesntTouchOmmitedFields() {
            String json = "{\"username\":\"frank\"}";
            String expectedJson = """
                        {
                            "username":"frank",
                            "displayname":"Frank",
                            "role": "admin",
                            "weight": 65.2,
                            "areWorkoutsPublic": true
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.patch().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(expectedJson));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql(sqlSelectExisting())
                    .query((rs) -> {
                        assertEquals("Frank", rs.getString("displayname"));
                        assertEquals("admin", rs.getString("user_role"));
                        assertEquals(true, rs.getBoolean("are_workouts_public"));
                        assertEquals(65.2d, rs.getDouble("weight"));
                    });
        }

        @Test
        public void seeOtherStatusWhenKeyChanges() {
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(resourceUri + "/{key}").build(newKey);

            String json = """
                        {
                            "username":"bob",
                            "displayname":"Bob",
                            "role": "default",
                            "weight": 75,
                            "areWorkoutsPublic": true
                        }
                    """;

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.put().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isEqualTo(303)
                                    .expectHeader().location(location.toString()),
                            r -> r.expectBody().json(json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql(sqlSelectNew())
                    .query((rs) -> {
                        assertEquals("Bob", rs.getString("displayname"));
                        assertEquals("default", rs.getString("user_role"));
                        assertEquals(true, rs.getBoolean("are_workouts_public"));
                        assertEquals(75d, rs.getDouble("weight"));
                    });

        }

        // Error tests
        @Test
        public void notFoundIfNotInDB() {
            String json = "{\"displayname\":\"frank69\", \"weight\": 65.33}";

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.patch().uri(resourceUri + "/{key}", newKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }
    }

    @Nested
    public class DeleteTests {
        @Test
        public void deletesInDB() {

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.delete().uri(resourceUri + "/{key}", existingKey)
                    .exchange()
                    .expectStatus().isNoContent()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore - 1, rowsAfter);
        }

        // Error tests
        @Test
        public void notFoundIfNotInDB() {

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.delete().uri(resourceUri + "/{key}", newKey)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }
    }

}
