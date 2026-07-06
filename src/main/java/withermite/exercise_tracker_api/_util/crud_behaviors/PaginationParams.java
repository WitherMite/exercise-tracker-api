package withermite.exercise_tracker_api._util.crud_behaviors;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

public record PaginationParams(
        @Min(0) @Max(128) Integer limit,
        @PositiveOrZero Integer offset) {

    public int getLimitOrDefault(int defaultLimit) {
        Integer limitParam = limit();
        return limitParam == null
                ? defaultLimit
                : limitParam;
    }

    public int getOffsetOrDefault(int defaultOffset) {
        Integer offsetParam = offset();
        return offsetParam == null
                ? defaultOffset
                : offsetParam;
    }
}
