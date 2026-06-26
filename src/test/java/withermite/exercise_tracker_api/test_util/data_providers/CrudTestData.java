package withermite.exercise_tracker_api.test_util.data_providers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class CrudTestData {
    public enum CaseType {
        FailNotEnoughFields(),
        FailUpdateNotExists(),

        ReadOneExisting(),
        ReadFiveExisting(),
        ReadThirdAndFourthExisting(),

        CreateOneUniqueMinimumFields(),

        ReplaceOneExisting(),
        ReplaceOneExistingMinumumFields(),
        ReplaceOneUnique(),
        ReplaceOneExistingNewKey(),

        UpdateOneExisting(),
        UpdateOneExistingMinimumFields(),
        UpdateOneExistingNewKey();
    }

    public final Map<CaseType, DataGroup> testCases;

    public CrudTestData(BiFunction<CaseType, DataGroup, DataGroup> dataGenerator) {
        this();
        this.testCases.replaceAll(dataGenerator);
    }

    CrudTestData() {
        this.testCases = new HashMap<>();
        for (CaseType key : CaseType.values()) {
            this.testCases.put(key, new DataGroup());
        }
    }
}
