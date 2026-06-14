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
        return usersRepository.one(username);
    }

    public User[] findMany() {
        return usersRepository.many();
    }

    public User update(String username, User user) {
        return usersRepository.update(username, user);
    }

    public void delete(String username) {
        usersRepository.delete(username);
    }
}
