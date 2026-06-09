package withermite.exercise_tracker_api.user;

import org.springframework.stereotype.Repository;

@Repository
public class UsersRepository {
    public void save(User user) {
        // save user to db
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
                new User("3", "three"),
        };
        return users;
    }

    public User replace(String username, User user) {
        // replace all fields in db
        return user;
    }

    public void delete(String username) {
        // remove from db
    }
}
