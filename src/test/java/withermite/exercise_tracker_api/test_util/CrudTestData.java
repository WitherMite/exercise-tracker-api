package withermite.exercise_tracker_api.test_util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CrudTestData {
    public class DataGroup {
        String inputJson;
        String expectedJson;
        Map<String, Object> expectedDbRowState;

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

        private DataGroup() {
            this.inputJson = null;
            this.expectedJson = null;
            this.expectedDbRowState = Map.of();
        }
    }

    // test cases to set, need better way to define them
    // maybe a map of these with enum as key?
    // or maybe make these abstract and inherit this class?
    // im not sure how to make this easiest to define and read
    // trying to keep the db state tied to the json cases is difficult...
    public DataGroup failNotEnoughFields = new DataGroup();
    public DataGroup failUpdateNotExists = new DataGroup();

    public DataGroup readOneExisting = new DataGroup();
    public DataGroup readFiveExisting = new DataGroup();
    public DataGroup readThirdAndFourthExisting = new DataGroup();

    public DataGroup createOneUniqueMinimumFields = new DataGroup();

    public DataGroup replaceOneExisting = new DataGroup();
    public DataGroup replaceOneExistingMinumumFields = new DataGroup();
    public DataGroup replaceOneUnique = new DataGroup();
    public DataGroup replaceOneExistingNewKey = new DataGroup();

    public DataGroup updateOneExisting = new DataGroup();
    public DataGroup updateOneExistingMinimumFields = new DataGroup();
    public DataGroup updateOneExistingNewKey = new DataGroup();

}
