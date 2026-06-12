package withermite.exercise_tracker_api.user;

import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository userRepository) {
        this.usersRepository = userRepository;
    }

    public User create(User user) {
        return usersRepository.save(user);
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
        return usersRepository.replace(username, user);
    }

    public User update(String username, User user) {
        // need to get jooq stuff implemented to do this,
        // too much work to actually test updating without it
        return user;
    }

    public void delete(String username) {
        usersRepository.delete(username);
    }
}
