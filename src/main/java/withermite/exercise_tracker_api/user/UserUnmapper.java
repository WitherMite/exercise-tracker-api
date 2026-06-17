package withermite.exercise_tracker_api.user;

import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;
import org.jooq.generated.enums.UserRoleEnum;
import org.jooq.generated.tables.records.AppUserRecord;

public class UserUnmapper implements RecordUnmapper<User, AppUserRecord> {
    public static void unmapDiff(User user, AppUserRecord record) {
        if (record == null)
            return;

        if (user.username != null && !record.getUsername().equals(user.username))
            record.setUsername(user.username);

        if (user.displayname != null && !record.getDisplayname().equals(user.displayname))
            record.setDisplayname(user.displayname);

        if (user.role != null && !record.getUserRole().getLiteral().equals(user.role))
            record.setUserRole(UserRoleEnum.lookupLiteral(user.role));

        if (user.weight != null && !record.getWeight().equals(user.weight))
            record.setWeight(user.weight);

        if (user.areWorkoutsPublic != null && !record.getAreWorkoutsPublic().equals(user.areWorkoutsPublic))
            record.setAreWorkoutsPublic(user.areWorkoutsPublic);
    }

    @Override
    public AppUserRecord unmap(User user) throws MappingException {
        try {
            AppUserRecord record = new AppUserRecord();
            record.setUsername(user.username);
            record.setDisplayname(user.displayname);
            record.setWeight(user.weight);
            record.setUserRole(UserRoleEnum.lookupLiteral(user.role));
            record.setAreWorkoutsPublic(user.areWorkoutsPublic);
            return record;
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new MappingException("user could not be mapped to jooq record", e);
        }
    }
}
