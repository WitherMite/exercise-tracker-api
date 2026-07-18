package withermite.exercise_tracker_api.user_workout;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.jooq.generated.enums.SubjectiveEffortTypeEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.Full;
import withermite.exercise_tracker_api._util.validation.constraints.IsEnumType;
import withermite.exercise_tracker_api.exercise.Exercise;

public class Workout implements Entity {
    @Null // manually added from uri / authed user / db
    String userUsername;

    // uses a public getter and package private setter so
    // jackson only serializes ids as responses
    private Integer id;

    public Exercise exercise;

    @PastOrPresent
    public Instant datetime;

    public String notes;

    @Positive
    public Short count;

    @IsEnumType(enumTypeClass = SubjectiveEffortTypeEnum.class, message = "Subjective effort type must be a valid subjective effort type")
    public String subjectiveEffortType;

    @Valid
    @NotNull(groups = Full.class)
    public List<WorkoutStatistic> statistics;

    @AssertTrue(message = "Need workout statistics for every count", groups = Full.class)
    @SuppressWarnings("unused")
    public boolean checkStatisticsLengthEqualCount() {
        if (statistics == null) {
            return false;
        }
        return count == statistics.size();
    }

    @Override
    public Map<String, String> fetchUriKeys() {
        return Map.of("key", id.toString(), "userUsername", userUsername);
    }

    public Integer getId() {
        return id;
    }

    void setId(Integer newId) {
        id = newId;
    }
}
