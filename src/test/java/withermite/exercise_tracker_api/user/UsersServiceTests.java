package withermite.exercise_tracker_api.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;

public class UsersServiceTests {

    // should be able to change shape of users whenever anyway
    // so can just use a simple mock with enough fields to test edge cases
    private final class User {
        public String username;

        public User(String username) {
            this.username = username;
        }
    }

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService usersService;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Crud tests
    @Test
    public void savesUserinRepository() {
        User testUser = new User("frank");

        usersService.create(testUser);

        verify(usersRepository, times(1)).save(testUser);
    }

    // Edge cases

}
