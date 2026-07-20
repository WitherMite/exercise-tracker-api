package withermite.exercise_tracker_api.login;

import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;
import org.jooq.generated.enums.UserRoleEnum;
import org.jooq.generated.tables.records.AppUserRecord;

import withermite.exercise_tracker_api._util.crud_behaviors.EntityMerger;

public class AuthUnmapper
        implements EntityMerger<AuthRequest, AppUserRecord>, RecordUnmapper<AuthRequest, AppUserRecord> {

    @Override
    public void unmapDiff(AuthRequest user, AppUserRecord record) {
        if (record == null)
            return;

        if (user.username != null && !record.getUsername().equals(user.username))
            record.setUsername(user.username);

        if (user.password != null && !record.getPwHash().equals(user.password))
            record.setPwHash(user.password);

        if (user.role != null && !record.getUserRole().getLiteral().equals(user.role))
            record.setUserRole(UserRoleEnum.lookupLiteral(user.role));
    }

    @Override
    public AppUserRecord unmap(AuthRequest user) throws MappingException {
        try {
            AppUserRecord record = new AppUserRecord();

            record.setUsername(user.username);

            if (user.role == null) {
                record.setUserRole(UserRoleEnum.default_);
            } else {
                record.setUserRole(UserRoleEnum.lookupLiteral(user.role));
            }

            return record;
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new MappingException("user could not be mapped to jooq record", e);
        }
    }
}
