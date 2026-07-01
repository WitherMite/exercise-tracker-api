package withermite.exercise_tracker_api.config;

import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.RecordType;
import org.jooq.RecordUnmapper;
import org.jooq.RecordUnmapperProvider;
import org.jooq.impl.DefaultRecordUnmapper;

import withermite.exercise_tracker_api.exercise_type.ExerciseType;
import withermite.exercise_tracker_api.exercise_type.ExerciseTypeUnmapper;
import withermite.exercise_tracker_api.user.User;
import withermite.exercise_tracker_api.user.UserUnmapper;

public class AppRecordUnmapperProvider implements RecordUnmapperProvider {
    private final Configuration config;
    private final UserUnmapper userUnmapper = new UserUnmapper();
    private final ExerciseTypeUnmapper exerciseTypeUnmapper = new ExerciseTypeUnmapper();

    public AppRecordUnmapperProvider(Configuration config) {
        this.config = config;
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
        return new DefaultRecordUnmapper<>(type, recordType, config);
    }
}
