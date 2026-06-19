package withermite.exercise_tracker_api.user;

import java.util.ArrayList;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import withermite.exercise_tracker_api.util.ResourceWrapper;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository userRepository) {
        this.usersRepository = userRepository;
    }

    public ResourceWrapper<User> create(User user) {
        if (user.username == null || user.displayname == null) {
            return null;
        }
        try {
            return new ResourceWrapper<>(usersRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            String causeMsg = e.getCause().getMessage();
            ArrayList<String> problems = new ArrayList<>();
            System.err.println(e.toString());
            System.err.println(causeMsg);
            if (causeMsg.contains("duplicate key value violates unique constraint \"user_natural_key\"")) {
                problems.add("username " + user.username + " taken");
            }
            return new ResourceWrapper<>(null, problems);
        }
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
