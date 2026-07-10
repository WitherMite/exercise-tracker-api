package withermite.exercise_tracker_api.user_workout;

import java.time.Duration;

import org.jooq.generated.enums.SubjectiveEffortTypeEnum;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import withermite.exercise_tracker_api._util.validation.constraints.IsEnumType;

public class WorkoutStatistic {
    public Double load;
    public Double workTime;
    public Duration restLength;

    @IsEnumType(enumTypeClass = SubjectiveEffortTypeEnum.class, message = "Subjective effort type must be a valid subjective effort type")
    public String subjectiveEffortType;

    @Min(0)
    @Max(20)
    public Float subjectiveEffortValue;
}
