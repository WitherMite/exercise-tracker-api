package withermite.exercise_tracker_api.user;

import org.jooq.DSLContext;
import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;
import org.jooq.generated.enums.UserRoleEnum;
import static org.jooq.generated.tables.AppUser.APP_USER;
import org.jooq.generated.tables.records.AppUserRecord;

import withermite.exercise_tracker_api._util.crud_behaviors.EntityMerger;

public class UserUnmapper implements EntityMerger<User, AppUserRecord>, RecordUnmapper<User, AppUserRecord> {
    private final DSLContext create;

    public UserUnmapper(DSLContext create) {
        this.create = create;
    }

    @Override
    public void unmapDiff(User user, AppUserRecord record) {
        if (record == null)
            return;

        if (user.username != null)
            record.setUsername(user.username);

        if (user.displayname != null)
            record.setDisplayname(user.displayname);

        if (user.role != null)
            record.setUserRole(UserRoleEnum.lookupLiteral(user.role));

        if (user.weight != null)
            record.setWeight(user.weight);

        if (user.areWorkoutsPublic != null)
            record.setAreWorkoutsPublic(user.areWorkoutsPublic);
    }

    @Override
    public AppUserRecord unmap(User user) throws MappingException {
        try {
            AppUserRecord record = create.newRecord(APP_USER);
            record.setUsername(user.username);
            record.setDisplayname(user.displayname);
            record.setWeight(user.weight);
            if (user.role == null) {
                record.setUserRole(UserRoleEnum.default_);
            } else {
                record.setUserRole(UserRoleEnum.lookupLiteral(user.role));
            }
            if (user.areWorkoutsPublic == null) {
                record.setAreWorkoutsPublic(false);
            } else {
                record.setAreWorkoutsPublic(user.areWorkoutsPublic);
            }
            return record;
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new MappingException("user could not be mapped to jooq record", e);
        }
    }
}
