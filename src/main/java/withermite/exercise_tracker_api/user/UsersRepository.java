package withermite.exercise_tracker_api.user;

import org.jooq.DSLContext;
import static org.jooq.generated.tables.AppUser.APP_USER;
import org.jooq.generated.tables.records.AppUserRecord;
import org.springframework.stereotype.Repository;

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
        // get from db
        return new User(username, "displayname");
    }

    public User[] many() {
        // get from db
        User[] users = {
                new User("1", "one"),
                new User("2", "two"),
                new User("3", "three"), };
        return users;
    }

    public User replace(String username, User user) {
        // replace all fields in db
        return user;
    }

    public User update(String username, User user) {
        // update non null fields in db, return new complete user
        return user;
    }

    public void delete(String username) {
        // remove from db
    }
}
