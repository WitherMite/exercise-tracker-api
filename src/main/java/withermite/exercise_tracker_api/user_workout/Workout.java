package withermite.exercise_tracker_api.user_workout;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api.exercise.Exercise;
import withermite.exercise_tracker_api.user.User;

public class Workout implements Entity {
    @Positive
    public Long id;

    public User user;

    public Exercise exercise;

    @PastOrPresent
    public Instant datetime;

    public String notes;

    @Positive
    public Integer count;

    public List<WorkoutStatistic> statistics;

    @AssertTrue(message = "Need workout statistics for every count")
    @SuppressWarnings("unused")
    public boolean checkStatisticsLengthEqualCount() {
        return count == statistics.size();
    }

    @Override
    public Map<String, String> fetchUriKeys() {
        return Map.of("key", id.toString());
    }
}
