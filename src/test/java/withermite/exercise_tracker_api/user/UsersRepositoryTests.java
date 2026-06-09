package withermite.exercise_tracker_api.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class UsersRepositoryTests {
    private final UsersRepository usersRepository = new UsersRepository();

    @Test
    public void returnsOneUserWithUsername() {
        String username = "frank";

        User user = usersRepository.one(username);

        assertEquals(username, user.username);
    }

    @Test
    public void returnsManyUsers() {
        User[] users = usersRepository.many();

        assertNotNull(users);
    }
}
