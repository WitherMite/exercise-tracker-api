package withermite.exercise_tracker_api.user_workout;

import java.time.Duration;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class WorkoutStatistic {
    public Double load;
    public Double workTime;
    public Duration restLength;

    @Min(0)
    @Max(20)
    public Float subjectiveEffortValue;
}
