package withermite.exercise_tracker_api.login;

import org.jooq.DSLContext;
import static org.jooq.generated.tables.AppUser.APP_USER;
import org.jooq.generated.tables.records.AppUserRecord;
import org.springframework.stereotype.Repository;

import withermite.exercise_tracker_api._util.crud_behaviors.CrudRepositoryBehavior;

@Repository
public class AuthRepository {

    private final CrudRepositoryBehavior<AuthRequest, AppUserRecord, String> crud;

    public AuthRepository(DSLContext dslContext) {
        this.crud = new CrudRepositoryBehavior<>(
                dslContext, APP_USER, APP_USER.USERNAME, AuthRequest.class, null); // will not be capable of editing
                                                                                   // without unmapper, but is fine here
    }

    public AuthRequest getOne(String username) {
        return crud.getOne(username);
    }
}
