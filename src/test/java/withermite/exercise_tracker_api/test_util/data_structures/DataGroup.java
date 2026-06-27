package withermite.exercise_tracker_api.test_util.data_structures;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DataGroup {
    public String inputJson;
    public String expectedJson;
    public Map<String, Object> expectedDbRowState;

    public void assertDbState(ResultSet rs) {
        if (expectedDbRowState.isEmpty()) {
            fail("no expected database state to assert");
        }

        expectedDbRowState.forEach((columnName, expectedValue) -> {
            try {
                assertEquals(expectedValue, rs.getObject(columnName, expectedValue.getClass()));
            } catch (SQLException e) {
                fail(e);
            }
        });
    }

    public DataGroup(String inputJson, String expectedJson, Map<String, Object> expectedDbRowState) {
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
