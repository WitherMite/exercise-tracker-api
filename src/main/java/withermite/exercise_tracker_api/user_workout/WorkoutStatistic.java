package withermite.exercise_tracker_api.user_workout;

import java.time.Duration;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;

public class WorkoutStatistic {
    public Double load;
    public Double workTime;
    public Duration restLength;

    @Min(0)
    @Max(20)
    public Double subjectiveEffortValue;

    // to be populated before mapping to a jooq record
    @Null
    Integer workoutId;
    @Null
    Short index;
}
