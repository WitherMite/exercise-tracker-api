package withermite.exercise_tracker_api.user;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public void save(User user) {
        // save user to db
    }

    public User one(String username) {
        // get from db
        return new User(username);
    }

    public User[] many() {
        // get from db
        User[] users = {
                new User("1"),
                new User("2"),
                new User("3"),
        };
        return users;
    }

    public User replace(String username, User user) {
        // replace all fields in db
        return new User(username);
    }

    public void delete(String username) {
        // remove from db
    }
}
