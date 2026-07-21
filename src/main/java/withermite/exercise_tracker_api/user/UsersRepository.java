package withermite.exercise_tracker_api.user;

import java.util.List;

import org.jooq.DSLContext;
import static org.jooq.generated.tables.AppUser.APP_USER;
import org.jooq.generated.tables.records.AppUserRecord;
import org.springframework.stereotype.Repository;

import withermite.exercise_tracker_api._util.ResourceWrapper;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudRepository;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudRepositoryBehavior;

@Repository
public class UsersRepository implements CrudRepository<User, String> {
    private final CrudRepositoryBehavior<User, AppUserRecord, String> crud;

    public UsersRepository(DSLContext dslContext) {
        this.crud = new CrudRepositoryBehavior<>(
                dslContext, APP_USER, APP_USER.USERNAME, User.class, new UserUnmapper(dslContext));
    }

    @Override
    public User save(User user) {
        return crud.save(user);
    }

    @Override
    public User getOne(String username) {
        return crud.getOne(username);
    }

    @Override
    public List<User> getMany(int pageSize, int offset) {
        return crud.getMany(pageSize, offset);
    }

    @Override
    public User update(String username, User user) {
        return crud.update(username, user);
    }

    @Override
    public ResourceWrapper<User> replace(String username, User user) {
        return crud.replace(username, user);
    }

    @Override
    public void delete(String username) {
        crud.delete(username);
    }
}
