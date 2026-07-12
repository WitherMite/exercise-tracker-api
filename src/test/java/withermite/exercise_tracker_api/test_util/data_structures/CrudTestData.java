package withermite.exercise_tracker_api.test_util.data_structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.exc.JsonNodeException;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import withermite.exercise_tracker_api.test_util.data_structures.DataGroup.ExpectedObject;

public class CrudTestData {
    private static final JsonMapper mapper = JsonMapper.builder().build();

    private static Object castJsonNode(JsonNode node, Class<?> type) {
        if (node.isNull())
            return null;

        if (type == null) {
            throw new RuntimeException("Node missing type:" + node.toPrettyString());
        }

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

    private static Map<String, ExpectedObject> buildDbStateMap(ObjectNode dbValues,
            Map<String, Class<?>> dbRowTypeMap) {
        Map<String, ExpectedObject> map = new HashMap<>();

        dbValues.forEachEntry((prop, val) -> {
            Class<?> type = dbRowTypeMap.get(prop);
            map.put(prop, new ExpectedObject(castJsonNode(val, type), type));
        });
        return map;
    }

    public static enum JsonShape {
        NotEnoughFields(),
        MinimumFields(),
        MinimumFieldsWithDefaults(),
        Full(),
        NotEnoughFieldsAlt(),
        NotEnoughAltMergeFull(),
        MinimumFieldsAlt(),
        MinFieldsAltWithDefaults(),
        FullAlt(),
    }

    private final ObjectNode rowFieldMap;
    private final ObjectNode defaultValues;
    private final List<String> optionalFields;
    private final Map<String, Class<?>> dbRowTypeMap;

    public final Map<JsonShape, DataGroup> existingEntities;
    public final Map<JsonShape, DataGroup> uniqueEntities;
    public final String fullPageExistingDefaultJson;
    public final String thirdAndFourthExistingJson;

    public CrudTestData(ObjectNode testData, Map<String, Class<?>> dbRowTypeMap) throws JsonNodeException {
        this.dbRowTypeMap = dbRowTypeMap;
        // grab nodes from testData json object
        ArrayNode optional = testData.get("optionalFields").asArray();
        ArrayNode fullPage = testData.get("defaultFullPageResponse").asArray();
        ObjectNode existingData = testData.get("entityExisting").asObject();
        ObjectNode uniqueData = testData.get("entityUnique").asObject();

        this.defaultValues = testData.get("defaultValues").asObject();
        this.rowFieldMap = testData.get("dbRowToFieldMapping").asObject();
        // set paginated json responses
        this.fullPageExistingDefaultJson = fullPage.toString();

        ArrayNode arrNode = mapper.createArrayNode()
                .add(fullPage.get(2)).add(fullPage.get(3));
        this.thirdAndFourthExistingJson = arrNode.toString();

        // build requiredFields array
        this.optionalFields = new ArrayList<>();
        optional.elements().forEach((node) -> optionalFields.add(node.asString()));

        // build maps for the new and stored entities
        this.existingEntities = new HashMap<>();
        this.uniqueEntities = new HashMap<>();

        for (JsonShape key : JsonShape.values()) {
            // set needed data shapes for each entity
            addJsonShape(this.existingEntities, key, existingData);
            addJsonShape(this.uniqueEntities, key, uniqueData);
        }
    }

    private void addJsonShape(Map<JsonShape, DataGroup> entities, JsonShape key, ObjectNode data) {
        final ObjectNode entity = data.get("original").asObject().deepCopy();
        Map<String, ExpectedObject> dbStateMap;

        // replace entity with alternate one if needed, and build db state map for each
        boolean isAlternate = key == JsonShape.NotEnoughFieldsAlt
                || key == JsonShape.MinimumFieldsAlt
                || key == JsonShape.FullAlt
                || key == JsonShape.MinFieldsAltWithDefaults
                || key == JsonShape.NotEnoughAltMergeFull;
        if (isAlternate) {
            ObjectNode entityAlt = data.get("alternateValues").asObject().deepCopy();
            ObjectNode dbDataAlt = data.get("dbStateAlternate").asObject().deepCopy();

            entity.setAll(entityAlt);
            dbStateMap = buildDbStateMap(dbDataAlt, dbRowTypeMap);
        } else {
            ObjectNode dbData = data.get("dbStateOriginal").asObject().deepCopy();
            dbStateMap = buildDbStateMap(dbData, dbRowTypeMap);
        }

        boolean isMinimum = key == JsonShape.MinimumFields
                || key == JsonShape.MinimumFieldsWithDefaults
                || key == JsonShape.MinimumFieldsAlt
                || key == JsonShape.MinFieldsAltWithDefaults;
        if (isMinimum) {
            // remove optional properties
            optionalFields.forEach((field) -> {
                entity.remove(field);
            });

            // replace removed values with default ones if needed
            // (assuming if has default it would be optional)
            boolean withDefaults = key == JsonShape.MinimumFieldsWithDefaults
                    || key == JsonShape.MinFieldsAltWithDefaults;
            if (withDefaults) {
                // json
                ObjectNode values = defaultValues.get("entity").asObject();
                values.forEachEntry((prop, val) -> {
                    entity.set(prop, val);
                });
                // dbStateMap
                ObjectNode rows = defaultValues.get("database").asObject();
                rows.forEachEntry((prop, val) -> {
                    Class<?> type = dbRowTypeMap.get(prop);
                    dbStateMap.put(prop, new ExpectedObject(castJsonNode(val, type), type));
                });
            }
        }

        boolean isNotEnoughFields = key == JsonShape.NotEnoughFields
                || key == JsonShape.NotEnoughFieldsAlt
                || key == JsonShape.NotEnoughAltMergeFull;
        if (isNotEnoughFields) {
            // remove all optional properties and half of required
            // starting from the first -> (0 % 2 == 0) == true
            optionalFields.forEach((field) -> {
                entity.remove(field);
            });
            String[] entityProps = entity.propertyNames().toArray(String[]::new);
            for (int i = 0; i < entityProps.length; i++) {
                if (i % 2 == 0) {
                    entity.remove(entityProps[i]);
                }
            }

            // merge remaining alternate fields to the existing entity
            if (key == JsonShape.NotEnoughAltMergeFull) {
                ObjectNode original = data.get("original").asObject();
                ObjectNode dbOriginal = data.get("dbStateOriginal").asObject();

                // merge db mapping first so we know what changed in entity
                dbOriginal.forEachEntry((row, val) -> {
                    // overwrite the replaced values in dbState,
                    // skip rows that have no mapping to api fields
                    if (!rowFieldMap.has(row))
                        return;
                    String field = rowFieldMap.get(row).asString();
                    if (!entity.has(field)) {
                        JsonNode value = dbOriginal.get(row);
                        Class<?> type = dbRowTypeMap.get(row);
                        dbStateMap.put(row, new ExpectedObject(castJsonNode(value, type), type));
                    }
                });

                original.forEachEntry((prop, val) -> {
                    // replace missing properties
                    if (!entity.has(prop)) {
                        entity.set(prop, original.get(prop));
                    }
                });
            }
        }

        entities.put(key, new DataGroup(entity.toString(), dbStateMap));
    }
}
