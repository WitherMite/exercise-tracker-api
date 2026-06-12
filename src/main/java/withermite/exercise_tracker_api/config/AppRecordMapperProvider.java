package withermite.exercise_tracker_api.config;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.impl.DefaultRecordMapper;

import withermite.exercise_tracker_api.user.User;
import withermite.exercise_tracker_api.user.UserMapper;

public class AppRecordMapperProvider implements RecordMapperProvider {
    @SuppressWarnings("unchecked")
    @Override
    public <R extends Record, E> RecordMapper<R, E> provide(RecordType<R> recordType, Class<? extends E> type) {
        if (User.class.isAssignableFrom(type)) {
            var mapper = new UserMapper();
            if (mapper instanceof RecordMapper) {
                return (RecordMapper<R, E>) mapper;
            }

        }

        return new DefaultRecordMapper<>(recordType, type);
    }
}