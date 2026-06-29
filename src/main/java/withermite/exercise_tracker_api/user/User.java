package withermite.exercise_tracker_api.user;

import withermite.exercise_tracker_api.util.crud_behaviors.Entity;

public class User implements Entity {
    public String username;
    public String displayname;
    public Double weight;
    public String role;
    public Boolean areWorkoutsPublic;

    public User(String username, String displayname) {
        this.username = username;
        this.displayname = displayname;
    }

    public User() {
    }

    @Override
    public String getKey() {
        return username;
    }
}
