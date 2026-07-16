package withermite.exercise_tracker_api.user_workout;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.jooq.generated.enums.SubjectiveEffortTypeEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.Full;
import withermite.exercise_tracker_api._util.validation.constraints.IsEnumType;
import withermite.exercise_tracker_api.exercise.Exercise;
import withermite.exercise_tracker_api.user.User;

public class Workout implements Entity {
    @Positive
    @NotNull(groups = { Full.class }, message = "Workout id must not be null")
    public Integer id;

    public Exercise exercise;

    public User user;

    @PastOrPresent
    public Instant datetime;

    public String notes;

    @Positive
    public Short count;

    @IsEnumType(enumTypeClass = SubjectiveEffortTypeEnum.class, message = "Subjective effort type must be a valid subjective effort type")
    public String subjectiveEffortType;

    @Valid
    public List<WorkoutStatistic> statistics;

    @AssertTrue(message = "Need workout statistics for every count")
    @SuppressWarnings("unused")
    public boolean checkStatisticsLengthEqualCount() {
        return count == statistics.size();
    }

    @Override
    public Map<String, String> fetchUriKeys() {
        return Map.of("key", id.toString(), "user-username", user.username);
    }
}
