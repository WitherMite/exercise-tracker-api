package withermite.exercise_tracker_api._util.crud_behaviors;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

public record PaginationParams(
        @Min(value = 0, message = "Limit must be between 0 and 128") @Max(value = 128, message = "Limit must be between 0 and 128") Integer limit,
        @PositiveOrZero(message = "Offset must be greater than 0") Integer offset) {

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
