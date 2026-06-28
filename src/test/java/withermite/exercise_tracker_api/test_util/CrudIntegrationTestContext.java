package withermite.exercise_tracker_api.test_util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.NullNode;
import tools.jackson.databind.node.ObjectNode;
import withermite.exercise_tracker_api.test_util.CrudIntegrationTestsConfig.DBRowType;
import withermite.exercise_tracker_api.test_util.data_structures.CrudTestData;
import withermite.exercise_tracker_api.test_util.data_structures.CrudTestData.CaseType;
import withermite.exercise_tracker_api.test_util.data_structures.DataGroup;
import withermite.exercise_tracker_api.test_util.data_structures.DataGroup.ExpectedObject;

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

    private static Object castJsonNode(JsonNode node, Class<?> type) {
        if (node.isNull())
            return null;

        return switch (type.getSimpleName()) {
            case "String" -> node.asString();
            case "Boolean" -> node.asBoolean();
            case "Byte" -> node.asInt();
            case "Short" -> node.asShort();
            case "Integer" -> node.asInt();
            case "Long" -> node.asLong();
            case "Float" -> node.asFloat();
            case "Double" -> node.asDouble();
            case "BigInteger" -> node.asBigInteger();
            case "BigDecimal" -> node.asDecimal();
            default -> node;
        };
    }

    String testName;
    String resourceUri;
    String tableName;
    String keyRowName;
    String existingKey;
    String newKey;
    ClassPathResource populateSql;
    CrudTestData testData;

    public CrudIntegrationTestContext(FileSystemResource json, Map<String, DBRowType> jsonPropToDbRowMap) {
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

                // build db state map
                JsonNode expectedJson = testCase.get("expectedJson");
                Map<String, ExpectedObject> expectedDbRowState = new HashMap<>();

                expectedJson.forEachEntry((prop, valNode) -> {
                    DBRowType row = jsonPropToDbRowMap.get(prop);
                    Object expectedVal = castJsonNode(valNode, row.type());

                    expectedDbRowState.put(
                            row.rowName(),
                            new ExpectedObject(expectedVal, row.type()));
                });

                return new DataGroup(
                        testCase.get("inputJson").toString(),
                        expectedJson.toString(),
                        expectedDbRowState);
            });

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
