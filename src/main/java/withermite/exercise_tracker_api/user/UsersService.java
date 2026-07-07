package withermite.exercise_tracker_api.user;

import java.util.List;

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
    public User create(User user) {
        return usersRepository.save(user);
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
        return usersRepository.replace(username, user);
    }

    @Override
    public User update(String username, User user) {
        return usersRepository.update(username, user);
    }

    @Override
    public void delete(String username) {
        usersRepository.delete(username);
    }
}
