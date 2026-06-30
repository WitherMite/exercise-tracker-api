package withermite.exercise_tracker_api.user;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import static org.jooq.generated.tables.AppUser.APP_USER;
import org.jooq.generated.tables.records.AppUserRecord;
import org.springframework.stereotype.Repository;

import withermite.exercise_tracker_api.util.ResourceWrapper;
import withermite.exercise_tracker_api.util.crud_behaviors.CrudRepositoryBehavior;

@Repository
public class UsersRepository {
    private final DSLContext create;
    private final CrudRepositoryBehavior<User, AppUserRecord, String> crud;

    public UsersRepository(DSLContext dslContext) {
        this.create = dslContext;
        this.crud = new CrudRepositoryBehavior<>(
                dslContext, APP_USER, APP_USER.USERNAME, User.class, new UserUnmapper());
    }

    public User save(User user) {
        AppUserRecord userRecord = create.newRecord(APP_USER, user);
        // temporary until adding security features
        userRecord.setPwHash("placeholder");
        userRecord.store();
        return userRecord.into(User.class);
    }

    public User getOne(String username) {
        return crud.getOne(username);
    }

    public List<User> getMany(int pageSize, int offset) {
        return crud.getMany(pageSize, offset);
    }

    public User update(String username, User user) {
        return crud.update(username, user);
    }

    public ResourceWrapper<User> replace(String username, User user) {
        // leave until security is implemented, has call to save, and needs pw_hash set
        try {
            // try to get user from db
            AppUserRecord userRecord = create.fetchOne(
                    APP_USER, APP_USER.USERNAME.eq(username));

            // if was in db, replace all values, and update
            if (userRecord != null) {
                userRecord.from(user);
                userRecord.update();
                return new ResourceWrapper<>(userRecord.into(User.class));
            }
            // if not in db create new user
            return new ResourceWrapper<>(save(user), true);

        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean delete(String username) {
        return crud.delete(username);
    }
}
