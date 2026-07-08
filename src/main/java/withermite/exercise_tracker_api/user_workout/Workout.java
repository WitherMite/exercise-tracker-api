package withermite.exercise_tracker_api.user_workout;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.jooq.generated.enums.SubjectiveEffortTypeEnum;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api._util.validation.constraints.IsEnumType;
import withermite.exercise_tracker_api.exercise.Exercise;
import withermite.exercise_tracker_api.user.User;

public class Workout implements Entity<Long> {
    @Positive
    public Long id;

    public User user;

    public Exercise exercise;

    @PastOrPresent
    public Instant datetime;

    public String notes;

    @Positive
    public Integer count;

    @Size(min = 1)
    public List<Double> load;

    @Size(min = 1)
    public List<Double> workTime;

    @Size(min = 1)
    public List<Duration> restLength;

    @Size(min = 1)
    public List<@IsEnumType(enumTypeClass = SubjectiveEffortTypeEnum.class, message = "Subjective effort type must be a valid subjective effort type") String> subjectiveEffortType;

    @Size(min = 1)
    public List<@PositiveOrZero @Max(20) Float> subjectiveEffortValue;

    @AssertTrue(message = "All workout statistic arrays must be the same length")
    @SuppressWarnings("unused")
    public boolean checkStatisticsEqualSize() {
        return load.size() == workTime.size()
                && workTime.size() == restLength.size()
                && restLength.size() == subjectiveEffortType.size()
                && subjectiveEffortType.size() == subjectiveEffortValue.size();
    }

    @Override
    public Long fetchKeyValue() {
        return id;
    }

}
