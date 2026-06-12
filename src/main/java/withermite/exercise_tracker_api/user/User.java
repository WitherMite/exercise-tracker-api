package withermite.exercise_tracker_api.user;

public class User {
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
}
