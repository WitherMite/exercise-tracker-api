package withermite.exercise_tracker_api.test_util.data_structures;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DataGroup {
    public record ExpectedObject(Object value, Class<?> type) {
    };

    private final Map<String, ExpectedObject> expectedDbRowState;

    public final String json;

    public DataGroup(String json, Map<String, ExpectedObject> expectedDbRowState) {
        this.json = json;
        this.expectedDbRowState = expectedDbRowState;
    }

    public void assertDbState(ResultSet rs) {
        if (expectedDbRowState == null || expectedDbRowState.isEmpty()) {
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
}
