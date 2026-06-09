package withermite.exercise_tracker_api.user;

public class User {
    // static void fromRecord() {
    // // create and return user DTO from a user record
    // }

    public String username;
    public String displayname;
    public Double weight;
    public String role;
    public Boolean areWorkoutsPublic;

    public User(String username, String displayname) {
        this.username = username;
        this.displayname = displayname;
    }
}
