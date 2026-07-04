package withermite.exercise_tracker_api.integration_tests;

import java.net.URI;

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

import withermite.exercise_tracker_api.test_util.data_structures.CrudTestData;
import withermite.exercise_tracker_api.test_util.data_structures.CrudTestData.JsonShape;
import withermite.exercise_tracker_api.test_util.data_structures.DataGroup;

@ClassTemplate
@ExtendWith(CrudIntegrationTestContextProvider.class)
@SpringBootTest
@AutoConfigureRestTestClient
@Sql("/db/seed-schema.sql")
@Sql(scripts = "/testsql/clean.sql", executionPhase = AFTER_TEST_CLASS)
public class CrudIntegrationTests {
    // tests assume database was empty, except for the populate script,
    // and resources have a sequential id field, even if is hidden from api

    @Autowired
    private RestTestClient rest;

    @Autowired
    private JdbcClient jdbc;

    @Autowired
    private DataSource dataSource;

    private final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    public ClassPathResource populateSql;
    public CrudTestData testData;
    public String resourceUri;
    public String tableName;
    public String keyRowName;
    public String existingKey;
    public String uniqueKey;

    private String sqlSelectExisting() {
        return "SELECT * FROM " + tableName + " WHERE " + keyRowName + " = '" + existingKey + "'";
    }

    private String sqlSelectNew() {
        return "SELECT * FROM " + tableName + " WHERE " + keyRowName + " = '" + uniqueKey + "'";
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
            DataGroup data = testData.existingEntities.get(JsonShape.Full);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.get().uri(resourceUri + "/{key}", existingKey)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(data.json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }

        // Pagination tests (need to fix expecting db order with no order by clause)

        @Test
        public void getsManyFromDB() {
            String json = testData.fullPageExistingDefaultJson;

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.get().uri(resourceUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }

        @Test
        public void getsSpecifiedCountAndOffsetFromDB() {
            String json = testData.thirdAndFourthExistingJson;

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.get().uri(resourceUri + "?limit=2&offset=2")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }

        // Error tests

        @Test
        public void notFoundIfRequestedNotInDB() {

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.get().uri(resourceUri + "/{key}", uniqueKey)
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
            DataGroup request = testData.uniqueEntities.get(JsonShape.MinimumFields);
            DataGroup expected = testData.uniqueEntities.get(JsonShape.MinimumFieldsWithDefaults);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.post().uri(resourceUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request.json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isCreated(),
                            r -> r.expectBody().json(expected.json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(1, rowsAfter - rowsBefore);

            jdbc.sql(sqlSelectNew())
                    .query((rs) -> {
                        assertEquals(rowsBefore + 1, rs.getInt("id"));
                        expected.assertDbState(rs);
                    });
        }

        // Error tests

        @Test
        public void badRequestIfMissingFields() {
            DataGroup data = testData.existingEntities.get(JsonShape.NotEnoughFields);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.post().uri(resourceUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.json)
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }

        @Test
        public void conflictIfKeyNotUnique() {
            DataGroup data = testData.existingEntities.get(JsonShape.Full);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.post().uri(resourceUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.json)
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
            DataGroup data = testData.existingEntities.get(JsonShape.FullAlt);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.put().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(data.json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql(sqlSelectExisting())
                    .query((rs) -> {
                        assertEquals(1, rs.getInt("id"));
                        data.assertDbState(rs);
                    });
        }

        @Test
        public void ommitedOptionalFieldsSetDefaultValues() {
            DataGroup data = testData.existingEntities.get(JsonShape.MinimumFieldsAlt);
            DataGroup response = testData.existingEntities.get(JsonShape.MinFieldsAltWithDefaults);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.put().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(response.json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql(sqlSelectExisting())
                    .query((rs) -> {
                        response.assertDbState(rs);
                    });
        }

        @Test
        public void putCreatesNewResourceWhenNotInDB() {
            DataGroup data = testData.uniqueEntities.get(JsonShape.Full);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.put().uri(resourceUri + "/{key}", uniqueKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isCreated(),
                            r -> r.expectBody().json(data.json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(1, rowsAfter - rowsBefore);

            jdbc.sql(sqlSelectNew())
                    .query((rs) -> {
                        assertEquals(rowsBefore + 1, rs.getInt("id"));
                        data.assertDbState(rs);
                    });
        }

        @Test
        public void seeOtherStatusWhenKeyChanges() {
            DataGroup data = testData.uniqueEntities.get(JsonShape.Full);
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(resourceUri + "/{key}").build(uniqueKey);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.put().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isEqualTo(303)
                                    .expectHeader().location(location.toString()),
                            r -> r.expectBody().json(data.json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql(sqlSelectNew())
                    .query((rs) -> {
                        assertEquals(1, rs.getInt("id"));
                        data.assertDbState(rs);
                    });
        }
        // Error tests

        @Test
        public void badRequestIfMissingFields() {
            DataGroup data = testData.existingEntities.get(JsonShape.NotEnoughFields);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.put().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.json)
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
            DataGroup data = testData.existingEntities.get(JsonShape.FullAlt);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.patch().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(data.json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql(sqlSelectExisting())
                    .query((rs) -> {
                        data.assertDbState(rs);
                    });
        }

        @Test
        public void doesntTouchOmmitedFields() {
            DataGroup data = testData.existingEntities.get(JsonShape.NotEnoughFieldsAlt);
            DataGroup expected = testData.existingEntities.get(JsonShape.NotEnoughAltMergeFull);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.patch().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isOk(),
                            r -> r.expectBody().json(expected.json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql(sqlSelectExisting())
                    .query((rs) -> {
                        expected.assertDbState(rs);
                    });
        }

        @Test
        public void seeOtherStatusWhenKeyChanges() {
            DataGroup data = testData.uniqueEntities.get(JsonShape.Full);
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(resourceUri + "/{key}").build(uniqueKey);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.put().uri(resourceUri + "/{key}", existingKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.json)
                    .exchange().expectAll(
                            r -> r.expectStatus().isEqualTo(303)
                                    .expectHeader().location(location.toString()),
                            r -> r.expectBody().json(data.json));

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);

            jdbc.sql(sqlSelectNew())
                    .query((rs) -> {
                        assertEquals(1, rs.getInt("id"));
                        data.assertDbState(rs);
                    });

        }

        // Error tests
        @Test
        public void notFoundIfNotInDB() {
            DataGroup data = testData.uniqueEntities.get(JsonShape.Full);

            int rowsBefore = countRowsInTable(jdbc, tableName);

            rest.patch().uri(resourceUri + "/{key}", uniqueKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(data.json)
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

            rest.delete().uri(resourceUri + "/{key}", uniqueKey)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody().isEmpty();

            int rowsAfter = countRowsInTable(jdbc, tableName);
            assertEquals(rowsBefore, rowsAfter);
        }
    }
}
