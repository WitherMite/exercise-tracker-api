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
        User created = usersService.create(user);

        if (created == null) {
            return ResponseEntity.badRequest().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}").buildAndExpand(user.username).toUri();
        return ResponseEntity.created(location).body(created);
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

        if (replaced.resource == null) {
            return ResponseEntity.badRequest().build();
        }

        if (replaced.wasCreated) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{username}").buildAndExpand(user.username).toUri();
            return ResponseEntity.created(location).body(replaced.resource);
        }

        return ResponseEntity.ok().body(replaced.resource);
    }

    @PatchMapping("/{key}")
    public User update(@PathVariable String key, @RequestBody User user) {
        return usersService.update(key, user);
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
