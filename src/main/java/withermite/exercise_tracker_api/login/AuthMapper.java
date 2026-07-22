package withermite.exercise_tracker_api.login;

import org.jooq.RecordMapper;
import org.jooq.generated.tables.records.AppUserRecord;

public class AuthMapper implements RecordMapper<AppUserRecord, AuthRequest> {
    @Override
    public AuthRequest map(AppUserRecord record) {
        AuthRequest req = new AuthRequest();
        req.username = record.getUsername();
        req.password = record.getPwHash();
        req.role = record.getUserRole().getLiteral();
        return req;
    }
}
