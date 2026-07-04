package withermite.exercise_tracker_api.user_workout;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api.exercise.Exercise;
import withermite.exercise_tracker_api.user.User;

public class Workout implements Entity<Long> {
    public Long id;
    public User user;
    public Exercise exercise;
    public Instant datetime;
    public Integer count;
    public List<Double> load;
    public List<Double> workTime;
    public List<Duration> restLength;
    public String subjectiveEffortType;
    public Float subjectiveEffortValue;

    @Override
    public Long fetchKeyValue() {
        return id;
    }

}
