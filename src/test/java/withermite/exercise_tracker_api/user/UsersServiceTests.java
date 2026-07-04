package withermite.exercise_tracker_api.user;

import org.junit.jupiter.api.AfterEach;
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

import withermite.exercise_tracker_api._util.ResourceWrapper;

public class UsersServiceTests {
    private AutoCloseable mocks;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService usersService;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    @SuppressWarnings("unused")
    void cleanUp() throws Exception {
        this.mocks.close();
    }

    // Crud tests
    @Test
    public void savesUserInRepository() {
        User user = new User();
        user.username = "frank";
        user.displayname = "Frank";

        usersService.create(user);

        verify(usersRepository, times(1)).save(user);
    }

    @Test
    public void getsUserFromRepository() {
        String username = "frank";
        User user = new User();
        user.username = username;
        when(usersRepository.getOne(anyString())).thenReturn(user);

        User foundUser = usersService.findOne(username);

        assertInstanceOf(User.class, foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    public void getsManyUsersFromRepository() {

        usersService.findMany(5, 0);

        verify(usersRepository, times(1)).getMany(5, 0);
    }

    @Test
    public void replacesUserInRepository() {
        String username = "frank";
        String displayname = "Frank";
        User user = new User();
        user.username = username;
        user.displayname = displayname;
        // different return user to make sure we return from repository
        User user2 = new User();
        when(usersRepository.replace(anyString(), eq(user))).thenReturn(new ResourceWrapper<>(user2));

        User changedUser = usersService.replace(username, user).resource;

        assertInstanceOf(User.class, changedUser);
        assertEquals(user2, changedUser);
    }

    @Test
    public void updatesUserInRepository() {
        String username = "frank";
        String displayname = "Frank";
        User user = new User();
        user.username = username;
        user.displayname = displayname;
        // different return user to make sure we return from repository
        User user2 = new User();
        when(usersRepository.update(anyString(), eq(user))).thenReturn(user2);

        User changedUser = usersService.update(username, user);

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
