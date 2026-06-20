package withermite.exercise_tracker_api.user;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import withermite.exercise_tracker_api.util.ResourceWrapper;

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
        User testUser = new User("frank", "Frank");

        usersService.create(testUser);

        verify(usersRepository, times(1)).save(testUser);
    }

    @Test
    public void getsUserFromRepository() {
        String username = "frank";
        String displayname = "Frank";
        User user = new User(username, displayname);
        when(usersRepository.one(anyString())).thenReturn(user);

        User foundUser = usersService.findOne(username);

        assertInstanceOf(User.class, foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    public void getsManyUsersFromRepository() {

        usersService.findMany(5, 0);

        verify(usersRepository, times(1)).many(5, 0);
    }

    @Test
    public void replacesUserInRepository() {
        String username = "frank";
        String displayname = "Frank";
        User user = new User(username, displayname);
        // different return user to make sure we return from repository
        User user2 = new User("bob", "Bob");
        when(usersRepository.replace(anyString(), eq(user))).thenReturn(new ResourceWrapper<>(user2));

        User changedUser = usersService.replace(username, user).resource;

        assertInstanceOf(User.class, changedUser);
        assertEquals(user2, changedUser);
    }

    @Test
    public void updatesUserInRepository() {
        String username = "frank";
        String displayname = "Frank";
        User user = new User(username, displayname);
        // different return user to make sure we return from repository
        User user2 = new User("bob", "Bob");
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

    @Test
    public void createShouldNotAllowIncompleteUsers() {
        String username = "frank";
        User user = new User(username, null);

        ResourceWrapper<User> result = usersService.create(user);

        verify(usersRepository, never()).save(any(User.class));
        assertEquals(null, result);
    }

    @Test
    public void replaceShouldNotAllowIncompleteUsers() {
        String username = "frank";
        User user = new User(username, null);

        User changedUser = usersService.replace(username, user).resource;

        verify(usersRepository, never()).replace(anyString(), any(User.class));
        assertEquals(null, changedUser);
    }
}
