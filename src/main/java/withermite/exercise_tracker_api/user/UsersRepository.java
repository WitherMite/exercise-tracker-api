package withermite.exercise_tracker_api.user;

import org.jooq.DSLContext;
import static org.jooq.generated.tables.AppUser.APP_USER;
import org.jooq.generated.tables.records.AppUserRecord;
import org.springframework.stereotype.Repository;

import static withermite.exercise_tracker_api.user.UserUnmapper.unmapDiff;

@Repository
public class UsersRepository {
    private final DSLContext create;

    public UsersRepository(DSLContext dslContext) {
        this.create = dslContext;
    }

    public User save(User user) {
        AppUserRecord userRecord = create.newRecord(APP_USER, user);
        // temporary until adding security features
        userRecord.setPwHash("placeholder");
        userRecord.store();
        return userRecord.into(User.class);
    }

    public User one(String username) {
        AppUserRecord userRecord = create
                .fetchOne(APP_USER, APP_USER.USERNAME.eq(username));
        if (userRecord != null) {
            return userRecord.into(User.class);
        }
        return null;
    }

    public User[] many() {
        // get from db
        User[] users = {
                new User("1", "one"),
                new User("2", "two"),
                new User("3", "three"), };
        return users;
    }

    public User update(String username, User user) {
        AppUserRecord userRecord = create
                .fetchOne(APP_USER, APP_USER.USERNAME.eq(username));

        unmapDiff(user, userRecord);
        if (userRecord != null) {
            userRecord.store();
            return userRecord.into(User.class);
        }
        return null;
    }

    public boolean delete(String username) {
        AppUserRecord userRecord = create
                .fetchOne(APP_USER, APP_USER.USERNAME.eq(username));
        if (userRecord != null) {
            userRecord.delete();
            return true;
        }
        return false;
    }
}
