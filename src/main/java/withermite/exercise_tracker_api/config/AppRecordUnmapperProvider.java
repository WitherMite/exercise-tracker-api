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

public class AppRecordUnmapperProvider implements RecordUnmapperProvider {
    private final Configuration config;
    private final UserUnmapper userUnmapper;
    private final ExerciseTypeUnmapper exerciseTypeUnmapper;
    private final ExerciseUnmapper exerciseUnmapper;

    public AppRecordUnmapperProvider(Configuration config) {
        this.config = config;
        DSLContext create = config.dsl();
        this.exerciseUnmapper = new ExerciseUnmapper(create);
        this.exerciseTypeUnmapper = new ExerciseTypeUnmapper(create);
        this.userUnmapper = new UserUnmapper(create);
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
        return new DefaultRecordUnmapper<>(type, recordType, config);
    }
}
