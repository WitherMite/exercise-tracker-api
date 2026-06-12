package withermite.exercise_tracker_api.user;

import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;
import org.jooq.generated.enums.UserRoleEnum;
import org.jooq.generated.tables.records.AppUserRecord;

public class UserUnmapper implements RecordUnmapper<User, AppUserRecord> {
    @Override
    public AppUserRecord unmap(User user) throws MappingException {
        AppUserRecord record = new AppUserRecord();
        record.setUsername(user.username);
        record.setDisplayname(user.displayname);
        record.setWeight(user.weight);
        record.setUserRole(UserRoleEnum.lookupLiteral(user.role));
        record.setAreWorkoutsPublic(user.areWorkoutsPublic);
        return record;
    }
}
