package withermite.exercise_tracker_api.user;

import withermite.exercise_tracker_api._util.crud_behaviors.Entity;

public class User implements Entity<String> {
    public String username;
    public String displayname;
    public Double weight;
    public String role;
    public Boolean areWorkoutsPublic;

    @Override
    public String getKey() {
        return username;
    }
}
