package withermite.exercise_tracker_api.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudControllerBehavior;
import withermite.exercise_tracker_api._util.crud_behaviors.PaginationParams;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.AsDelta;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.Full;

@RestController
@RequestMapping("/users")
class UsersController {
    private final String resourceUri = "/users";
    private final CrudControllerBehavior<User, UsersService> crud;

    public UsersController(UsersService usersService, @Value("${user.defaultPageSize}") int defaultPageSize) {
        this.crud = new CrudControllerBehavior<>(usersService, this.resourceUri, defaultPageSize);
    }

    @GetMapping
    public ResponseEntity<List<User>> getMany(@Valid PaginationParams params) {
        return crud.getMany(params);
    }

    @PostMapping
    public ResponseEntity<User> create(@Validated(Full.class) @RequestBody User user) {
        return crud.create(user);
    }

    @GetMapping("/{key}")
    public ResponseEntity<User> getOne(@PathVariable String key) {
        return crud.getOne(key);
    }

    @PutMapping("/{key}")
    public ResponseEntity<User> replace(@PathVariable String key, @Validated(Full.class) @RequestBody User user) {
        return crud.replace(key, user);
    }

    @PatchMapping("/{key}")
    public ResponseEntity<User> update(@PathVariable String key, @Validated(AsDelta.class) @RequestBody User user) {
        return crud.update(key, user);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable String key) {
        return crud.delete(key);
    }
}
