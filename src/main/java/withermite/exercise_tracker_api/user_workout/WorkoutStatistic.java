package withermite.exercise_tracker_api.user_workout;

import java.time.Duration;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;

public class WorkoutStatistic {
    @PositiveOrZero(message = "Load must be greater then or equal to 0")
    public Double load;
    @PositiveOrZero(message = "Work time must be greater then or equal to 0")
    public Double workTime;
    public Duration restLength;

    @Min(value = 0, message = "Subjective effort value must be between 0 and 20 inclusive")
    @Max(value = 20, message = "Subjective effort value must be between 0 and 20 inclusive")
    public Double subjectiveEffortValue;

    // to be populated before mapping to a jooq record
    @Null
    Integer workoutId;
    @Null
    Short index;
}
