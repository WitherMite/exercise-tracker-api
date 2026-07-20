package withermite.exercise_tracker_api.config;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordType;
import org.jooq.RecordUnmapper;
import org.jooq.RecordUnmapperProvider;
import org.jooq.impl.DefaultRecordUnmapper;

import withermite.exercise_tracker_api.exercise.Exercise;
import withermite.exercise_tracker_api.exercise.ExerciseUnmapper;
import withermite.exercise_tracker_api.exercise_type.ExerciseType;
import withermite.exercise_tracker_api.exercise_type.ExerciseTypeUnmapper;
import withermite.exercise_tracker_api.user.User;
import withermite.exercise_tracker_api.user.UserUnmapper;
import withermite.exercise_tracker_api.user_workout.Workout;
import withermite.exercise_tracker_api.user_workout.WorkoutStatistic;
import withermite.exercise_tracker_api.user_workout.WorkoutStatisticUnmapper;
import withermite.exercise_tracker_api.user_workout.WorkoutUnmapper;

public class AppRecordUnmapperProvider implements RecordUnmapperProvider {
    private final Configuration config;
    private final UserUnmapper userUnmapper;
    private final ExerciseTypeUnmapper exerciseTypeUnmapper;
    private final ExerciseUnmapper exerciseUnmapper;
    private final WorkoutUnmapper workoutUnmapper;
    private final WorkoutStatisticUnmapper workoutStatisticUnmapper;

    public AppRecordUnmapperProvider(Configuration config) {
        this.config = config;
        DSLContext create = config.dsl();
        this.exerciseUnmapper = new ExerciseUnmapper(create);
        this.exerciseTypeUnmapper = new ExerciseTypeUnmapper(create);
        this.userUnmapper = new UserUnmapper(create);
        this.workoutUnmapper = new WorkoutUnmapper(create);
        this.workoutStatisticUnmapper = new WorkoutStatisticUnmapper(create);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, R extends Record> RecordUnmapper<E, R> provide(
            Class<? extends E> type, RecordType<R> recordType) {

        if (User.class.isAssignableFrom(type)) {
            return (RecordUnmapper<E, R>) userUnmapper;
        }
        if (ExerciseType.class.isAssignableFrom(type)) {
            return (RecordUnmapper<E, R>) exerciseTypeUnmapper;
        }
        if (Exercise.class.isAssignableFrom(type)) {
            return (RecordUnmapper<E, R>) exerciseUnmapper;
        }
        if (Workout.class.isAssignableFrom(type)) {
            return (RecordUnmapper<E, R>) workoutUnmapper;
        }
        if (WorkoutStatistic.class.isAssignableFrom(type)) {
            return (RecordUnmapper<E, R>) workoutStatisticUnmapper;
        }
        return new DefaultRecordUnmapper<>(type, recordType, config);
    }
}
