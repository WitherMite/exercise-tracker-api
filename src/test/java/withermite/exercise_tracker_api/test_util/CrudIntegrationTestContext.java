package withermite.exercise_tracker_api.test_util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.NullNode;
import tools.jackson.databind.node.ObjectNode;
import withermite.exercise_tracker_api.test_util.data_structures.CrudTestData;
import withermite.exercise_tracker_api.test_util.data_structures.CrudTestData.CaseType;
import withermite.exercise_tracker_api.test_util.data_structures.DataGroup;

public class CrudIntegrationTestContext {
    private static final JsonMapper mapper = JsonMapper.builder().build();

    private static void writeConfigStructureJson(FileSystemResource json) {
        // create root node for tree
        ObjectNode blankConfig = mapper.createObjectNode();
        // generate empty string fields
        blankConfig.put("testName", "");
        blankConfig.put("resourceUri", "");
        blankConfig.put("tableName", "");
        blankConfig.put("keyRowName", "");
        blankConfig.put("existingKey", "");
        blankConfig.put("newKey", "");
        blankConfig.put("populateSqlPath", "");
        // generate empty test cases
        ObjectNode testDataJson = blankConfig.putObject("testData");
        for (CaseType type : CaseType.values()) {
            // create an object for each test case with blank object fields
            ObjectNode testCase = testDataJson.putObject(type.toString());
            testCase.putObject("inputJson");
            testCase.putObject("expectedJson");
        }
        // write to file
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(json.getFile(), blankConfig);
    }

    String testName;
    String resourceUri;
    String tableName;
    String keyRowName;
    String existingKey;
    String newKey;
    ClassPathResource populateSql;
    CrudTestData testData;

    public CrudIntegrationTestContext(FileSystemResource json, Map<CaseType, Map<String, Object>> dbStateMap) {
        try (InputStream stream = json.getInputStream()) {
            // traverse the stream as a tree
            JsonNode node = mapper.readTree(stream);

            // if file is empty write blank structure to file and skip init
            if (node == null || node.isNull() || node.isEmpty()) {
                writeConfigStructureJson(json);
                return;
            }

            // set string properties
            this.testName = node.get("testName").asString();
            this.resourceUri = node.get("resourceUri").asString();
            this.tableName = node.get("tableName").asString();
            this.keyRowName = node.get("keyRowName").asString();
            this.existingKey = node.get("existingKey").asString();
            this.newKey = node.get("newKey").asString();
            this.populateSql = new ClassPathResource(node.get("populateSqlPath").asString());

            // populate test cases
            JsonNode testDataJson = node.get("testData");

            this.testData = new CrudTestData((key, data) -> {
                JsonNode testCase = testDataJson.get(key.toString());

                if (testCase == null || testCase instanceof NullNode)
                    return data;

                return new DataGroup(
                        testCase.get("inputJson").toString(),
                        testCase.get("expectedJson").toString(),
                        dbStateMap.get(key));
            });

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
