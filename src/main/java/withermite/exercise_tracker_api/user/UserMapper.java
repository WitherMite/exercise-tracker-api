package withermite.exercise_tracker_api.user;

import org.jooq.RecordMapper;
import org.jooq.generated.tables.records.AppUserRecord;

public class UserMapper implements RecordMapper<AppUserRecord, User> {
    @Override
    public User map(AppUserRecord record) {
        User user = new User();
        user.username = record.getUsername();
        user.displayname = record.getDisplayname();
        user.weight = record.getWeight();
        user.role = record.getUserRole().getLiteral();
        user.areWorkoutsPublic = record.getAreWorkoutsPublic();
        return user;
    }
}
