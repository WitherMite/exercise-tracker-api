package withermite.exercise_tracker_api.user;

import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UserRepository userRepository;

    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        userRepository.save(user);
        return user;
    }

    public User findOne(String username) {
        User user = userRepository.one(username);
        return user;
    }

    public User[] findMany() {
        User[] users = userRepository.many();
        return users;
    }

    public User replace(String username, User user) {
        userRepository.replace(username, user);
        return user;
    }

    public void delete(String username) {
        userRepository.delete(username);
    }
}
