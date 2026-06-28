package withermite.exercise_tracker_api.test_util.data_structures;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DataGroup {
    public final String inputJson;
    public final String expectedJson;
    private final Map<String, ExpectedObject> expectedDbRowState;

    public record ExpectedObject(Object value, Class<?> type) {
    };

    public void assertDbState(ResultSet rs) {
        if (expectedDbRowState.isEmpty()) {
            fail("no expected database state to assert");
        }

        expectedDbRowState.forEach((columnName, expected) -> {
            try {
                assertEquals(expected.value(), rs.getObject(columnName, expected.type()));
            } catch (SQLException e) {
                fail(e);
            }
        });
    }

    public DataGroup(String inputJson, String expectedJson, Map<String, ExpectedObject> expectedDbRowState) {
        this.inputJson = inputJson;
        this.expectedJson = expectedJson;
        this.expectedDbRowState = expectedDbRowState;
    }

    public DataGroup(String inputJson, String expectedJson) {
        this.inputJson = inputJson;
        this.expectedJson = expectedJson;
        this.expectedDbRowState = Map.of();
    }

    public DataGroup(String expectedJson) {
        this.inputJson = null;
        this.expectedJson = expectedJson;
        this.expectedDbRowState = Map.of();
    }

    protected DataGroup() {
        this.inputJson = null;
        this.expectedJson = null;
        this.expectedDbRowState = Map.of();
    }
}
