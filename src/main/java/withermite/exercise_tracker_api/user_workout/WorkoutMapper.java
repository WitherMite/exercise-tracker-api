package withermite.exercise_tracker_api.user_workout;

import java.util.List;

import org.jooq.RecordMapper;
import static org.jooq.generated.Keys.USER_WORKOUT_STATISTIC__USER_WORKOUT_STATISTIC_USER_WORKOUT_ID_FKEY;
import static org.jooq.generated.Keys.USER_WORKOUT__USER_WORKOUT_EXERCISE_ID_FKEY;
import static org.jooq.generated.Keys.USER_WORKOUT__USER_WORKOUT_USER_ID_FKEY;
import org.jooq.generated.tables.records.UserWorkoutRecord;

import withermite.exercise_tracker_api.exercise.Exercise;

public class WorkoutMapper implements RecordMapper<UserWorkoutRecord, Workout> {

    @Override
    public Workout map(UserWorkoutRecord record) {
        Workout workout = new Workout();

        workout.setId(record.getId());
        workout.datetime = record.getDatetime();
        workout.count = record.getWorkoutCount();
        workout.notes = record.getNotes();
        workout.subjectiveEffortType = record.getSubjectiveEffortType().getLiteral();

        workout.userUsername = record.fetchParent(USER_WORKOUT__USER_WORKOUT_USER_ID_FKEY).getUsername();

        Exercise exercise = record
                .fetchParent(USER_WORKOUT__USER_WORKOUT_EXERCISE_ID_FKEY)
                .into(Exercise.class);
        workout.exercise = exercise;

        List<WorkoutStatistic> statistics = record
                .fetchChildren(USER_WORKOUT_STATISTIC__USER_WORKOUT_STATISTIC_USER_WORKOUT_ID_FKEY)
                .into(WorkoutStatistic.class);
        workout.statistics = statistics;

        return workout;
    }
}
