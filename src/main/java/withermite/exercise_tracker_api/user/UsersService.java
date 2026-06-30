package withermite.exercise_tracker_api.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import withermite.exercise_tracker_api._util.ResourceWrapper;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudService;

@Service
public class UsersService implements CrudService<User> {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository userRepository) {
        this.usersRepository = userRepository;
    }

    @Override
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

    @Override
    public User findOne(String username) {
        return usersRepository.getOne(username);
    }

    @Override
    public List<User> findMany(int pageSize, int offset) {
        return usersRepository.getMany(pageSize, offset);
    }

    @Override
    public ResourceWrapper<User> replace(String username, User user) {
        if (user.username == null || user.displayname == null) {
            return new ResourceWrapper<>(null);
        }
        return usersRepository.replace(username, user);
    }

    @Override
    public User update(String username, User user) {
        return usersRepository.update(username, user);
    }

    @Override
    public boolean delete(String username) {
        return usersRepository.delete(username);
    }
}
