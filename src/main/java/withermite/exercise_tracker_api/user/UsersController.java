package withermite.exercise_tracker_api.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return usersService.create(user);
    }

    @GetMapping("/{key}")
    public User one(@PathVariable String key) {
        return usersService.findOne(key);
    }

    @PutMapping("/{key}")
    public ResponseEntity<User> replace(@PathVariable String key, @RequestBody User user) {
        if (!key.equals(user.username)) {
            User created = usersService.create(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }

        User replaced = usersService.update(key, user);
        return ResponseEntity.ok(replaced);
    }

    @PatchMapping("/{key}")
    public User update(@PathVariable String key, @RequestBody User user) {
        return usersService.update(key, user);
    }

    @DeleteMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String key) {
        usersService.delete(key);
    }
}
