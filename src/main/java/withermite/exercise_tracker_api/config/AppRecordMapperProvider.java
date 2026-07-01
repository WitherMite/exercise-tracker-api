package withermite.exercise_tracker_api.config;

import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.impl.DefaultRecordMapper;

import withermite.exercise_tracker_api.exercise_type.ExerciseType;
import withermite.exercise_tracker_api.exercise_type.ExerciseTypeMapper;
import withermite.exercise_tracker_api.user.User;
import withermite.exercise_tracker_api.user.UserMapper;

public class AppRecordMapperProvider implements RecordMapperProvider {
    private final Configuration config;
    private final UserMapper userMapper = new UserMapper();
    private final ExerciseTypeMapper exerciseTypeMapper = new ExerciseTypeMapper();

    public AppRecordMapperProvider(Configuration config) {
        this.config = config;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends Record, E> RecordMapper<R, E> provide(
            RecordType<R> recordType, Class<? extends E> type) {

        if (User.class.isAssignableFrom(type)) {
            return (RecordMapper<R, E>) userMapper;
        }
        if (ExerciseType.class.isAssignableFrom(type)) {
            return (RecordMapper<R, E>) exerciseTypeMapper;
        }
        return new DefaultRecordMapper<>(recordType, type, config);
    }
}