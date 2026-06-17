package withermite.exercise_tracker_api.user;

import org.springframework.stereotype.Service;

import withermite.exercise_tracker_api.util.ResourceWrapper;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository userRepository) {
        this.usersRepository = userRepository;
    }

    public User create(User user) {
        if (user.username == null || user.displayname == null) {
            return null;
        }
        return usersRepository.save(user);
    }

    public User findOne(String username) {
        return usersRepository.one(username);
    }

    public User[] findMany() {
        return usersRepository.many();
    }

    public ResourceWrapper<User> replace(String username, User user) {
        if (user.username == null || user.displayname == null) {
            return new ResourceWrapper<>(null);
        }
        return usersRepository.replace(username, user);
    }

    public User update(String username, User user) {
        return usersRepository.update(username, user);
    }

    public boolean delete(String username) {
        return usersRepository.delete(username);
    }
}
