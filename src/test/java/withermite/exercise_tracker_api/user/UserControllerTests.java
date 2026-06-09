package withermite.exercise_tracker_api.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

@WebMvcTest(UsersController.class)
@AutoConfigureRestTestClient
public class UserControllerTests {

    @Autowired
    private RestTestClient rest;

    @MockitoBean
    private UsersService usersService;

    private final ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);

    // Crud requests
    @Test
    public void getsUserFromService() throws Exception {
        String username = "frank";
        String displayname = "Frank";
        User user = new User(username, displayname);

        when(usersService.findOne(username)).thenReturn(user);

        rest.get().uri("/users/{username}", username)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectAll(
                        r -> r.expectStatus().isOk(),
                        r -> r.expectBody().json("{\"username\":\"frank\", \"displayname\":\"Frank\"}"));
    }

    @Test
    public void getsManyUsersFromService() throws Exception {
        User[] users = {
                new User("frank", "Frank"),
                new User("bob", "Bob")
        };

        when(usersService.findMany()).thenReturn(users);

        rest.get().uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectAll(
                        r -> r.expectStatus().isOk(),
                        r -> r.expectBody().json("[{\"username\":\"frank\"},{\"username\":\"bob\"}]"));
    }

    @Test
    public void postsUserToService() throws Exception {
        String json = "{\"username\":\"frank\", \"displayname\":\"Frank\"}";
        User user = new User("frank", "Frank");

        when(usersService.create(any(User.class))).thenReturn(new User("response", "Response"));

        // responds with body from service
        rest.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .exchange().expectAll(
                        r -> r.expectStatus().isCreated(),
                        r -> r.expectBody().json("{\"username\":\"response\", \"displayname\":\"Response\"}"));

        // service recieves user shaped like request json
        verify(usersService).create(argument.capture());
        assertEquals(user.username, argument.getValue().username);
        assertEquals(user.displayname, argument.getValue().displayname);
    }

    @Test
    public void putsUserToService() throws Exception {
        String json = "{\"username\":\"frank\", \"displayname\":\"Frank\"}";
        User user = new User("frank", "Frank");

        when(usersService.replace(eq("frank"), any(User.class))).thenReturn(new User("response", "Response"));

        // responds with body from service
        rest.put().uri("/users/frank")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .exchange().expectAll(
                        r -> r.expectStatus().isOk(),
                        r -> r.expectBody().json("{\"username\":\"response\", \"displayname\":\"Response\"}"));

        // service recieves user shaped like request json
        verify(usersService).replace(anyString(), argument.capture());
        assertEquals(user.username, argument.getValue().username);
        assertEquals(user.displayname, argument.getValue().displayname);
    }

    @Test
    public void deletesUserThroughService() throws Exception {
        String username = "frank";

        rest.delete().uri("/users/{username}", username)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        verify(usersService).delete(username);
    }

    // Edge cases

    @Test
    public void putCreatesNewUserWhenServiceCantReplace() throws Exception {
        String json = "{\"username\":\"frank\", \"displayname\":\"Frank\"}";
        User user = new User("frank", "Frank");

        when(usersService.create(any(User.class))).thenReturn(new User("response", "Response"));

        // responds with body from service create method, and created status
        rest.put().uri("/users/frank")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .exchange().expectAll(
                        r -> r.expectStatus().isCreated(),
                        r -> r.expectBody().json("{\"username\":\"response\", \"displayname\":\"Response\"}"));

        // service recieves attempt to replace, but is no mock so fails
        verify(usersService, times(1)).replace(eq("frank"), any(User.class));
        // and user shaped like request json to create
        verify(usersService).create(argument.capture());
        assertEquals(user.username, argument.getValue().username);
        assertEquals(user.displayname, argument.getValue().displayname);
    }
}
