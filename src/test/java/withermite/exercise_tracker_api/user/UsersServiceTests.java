package withermite.exercise_tracker_api.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class UsersServiceTests {

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
    public void savesUserInRepository() {
        User testUser = new User("frank");

        usersService.create(testUser);

        verify(usersRepository, times(1)).save(testUser);
    }

    @Test
    public void getsUserFromRepository() {
        String username = "frank";
        User user = new User(username);
        when(usersRepository.one(anyString())).thenReturn(user);

        User foundUser = usersService.findOne(username);

        assertInstanceOf(User.class, foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    public void getsManyUsersFromRepository() {

        usersService.findMany();

        verify(usersRepository, times(1)).many();
    }

    @Test
    public void replacesUserInRepository() {
        String username = "frank";
        User user = new User(username);
        // different return user to make sure we return from repository
        User user2 = new User("bob");
        when(usersRepository.replace(anyString(), eq(user))).thenReturn(user2);

        User changedUser = usersService.replace(username, user);

        assertInstanceOf(User.class, changedUser);
        assertEquals(user2, changedUser);
    }

    @Test
    public void deletesUserFromRepository() {
        String username = "frank";

        usersService.delete(username);

        verify(usersRepository, times(1)).delete(username);
    }

    // Edge cases

}
