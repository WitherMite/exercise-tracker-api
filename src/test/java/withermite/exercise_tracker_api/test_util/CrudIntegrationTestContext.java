package withermite.exercise_tracker_api.test_util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.exc.JsonNodeException;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;
import withermite.exercise_tracker_api.test_util.data_structures.CrudTestData;

public class CrudIntegrationTestContext {
    private static final JsonMapper mapper = JsonMapper.builder().build();

    private static void buildBlankEntityJson(ObjectNode entity) {
        entity.put("keyValue", "");
        entity.putObject("original");
        entity.putObject("alternateValues");
        entity.putObject("dbStateOriginal");
        entity.putObject("dbStateAlternate");
    }

    private static void writeConfigStructureJson(FileSystemResource json) {
        // create root node for tree
        ObjectNode blankConfig = mapper.createObjectNode();

        // generate empty fields
        blankConfig.put("testName", "");
        blankConfig.put("resourceUri", "");
        blankConfig.put("populateSqlPath", "");
        blankConfig.put("dbTableName", "");
        blankConfig.put("dbKeyRowName", "");

        // generate empty test data object
        ObjectNode testDataJson = blankConfig.putObject("testData");

        testDataJson.putArray("optionalFields");
        testDataJson.putObject("dbRowToFieldMapping");

        ObjectNode defaultVals = testDataJson.putObject("defaultValues");
        defaultVals.putObject("entity");
        defaultVals.putObject("database");

        ObjectNode existing = testDataJson.putObject("entityExisting");
        buildBlankEntityJson(existing);
        ObjectNode unique = testDataJson.putObject("entityUnique");
        buildBlankEntityJson(unique);

        testDataJson.putArray("defaultFullPageResponse");

        // write to file
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(json.getFile(), blankConfig);
    }

    public String testName;
    public String resourceUri;
    public String tableName;
    public String keyRowName;
    public String existingKey;
    public String newKey;
    public ClassPathResource populateSql;
    public CrudTestData testData;

    public CrudIntegrationTestContext(FileSystemResource json, Map<String, Class<?>> dbRowToTypeMap) {
        try (InputStream stream = json.getInputStream()) {
            // traverse the stream as a tree
            JsonNode node = mapper.readTree(stream);

            // if file is empty write blank structure to file and skip init
            if (node == null || node.isNull() || node.isEmpty()) {
                writeConfigStructureJson(json);
                return;
            }

            ObjectNode testDataJson = node.get("testData").asObject();

            // set properties
            this.testName = node.get("testName").asString();
            this.resourceUri = node.get("resourceUri").asString();
            this.tableName = node.get("dbTableName").asString();
            this.keyRowName = node.get("dbKeyRowName").asString();
            this.existingKey = testDataJson.get("entityExisting").get("keyValue").asString();
            this.newKey = testDataJson.get("entityUnique").get("keyValue").asString();
            this.populateSql = new ClassPathResource(node.get("populateSqlPath").asString());

            this.testData = new CrudTestData(testDataJson, dbRowToTypeMap);

        } catch (JsonNodeException e) {
            // repair config safely?
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
