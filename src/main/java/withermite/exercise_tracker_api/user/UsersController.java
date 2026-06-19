package withermite.exercise_tracker_api.user;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import withermite.exercise_tracker_api.util.ResourceWrapper;

@RestController
@RequestMapping("/users")
class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public User[] many() {
        return usersService.findMany();
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        ResourceWrapper<User> created = usersService.create(user);

        if (created == null) {
            return ResponseEntity.badRequest().build();
        }

        if (created.problems != null) {
            return ResponseEntity.status(409).build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}").buildAndExpand(user.username).toUri();
        return ResponseEntity.created(location).body(created.resource);
    }

    @GetMapping("/{key}")
    public ResponseEntity<User> one(@PathVariable String key) {
        User user = usersService.findOne(key);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{key}")
    public ResponseEntity<User> replace(@PathVariable String key, @RequestBody User user) {
        ResourceWrapper<User> replaced = usersService.replace(key, user);
        User replacedUser = replaced.resource;

        if (replacedUser == null) {
            return ResponseEntity.badRequest().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users/{username}").buildAndExpand(replacedUser.username).toUri();

        if (replaced.wasCreated) {
            return ResponseEntity.created(location).body(replacedUser);
        }

        if (!key.equals(replacedUser.username)) {
            return ResponseEntity.status(303)
                    .header("Location", location.toString())
                    .body(replacedUser);
        }

        return ResponseEntity.ok().body(replacedUser);
    }

    @PatchMapping("/{key}")
    public ResponseEntity<User> update(@PathVariable String key, @RequestBody User user) {
        User newUser = usersService.update(key, user);

        if (!key.equals(newUser.username)) {
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/users/{username}").buildAndExpand(newUser.username).toUri();

            return ResponseEntity.status(303)
                    .header("Location", location.toString())
                    .body(newUser);
        }

        return ResponseEntity.ok().body(newUser);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable String key) {
        boolean deleted = usersService.delete(key);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
