package withermite.exercise_tracker_api.user;

import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository userRepository) {
        this.usersRepository = userRepository;
    }

    public <T> T create(T user) {
        usersRepository.save(user);
        return user;
    }

    public User findOne(String username) {
        User user = usersRepository.one(username);
        return user;
    }

    public User[] findMany() {
        User[] users = usersRepository.many();
        return users;
    }

    public User replace(String username, User user) {
        usersRepository.replace(username, user);
        return user;
    }

    public void delete(String username) {
        usersRepository.delete(username);
    }
}
