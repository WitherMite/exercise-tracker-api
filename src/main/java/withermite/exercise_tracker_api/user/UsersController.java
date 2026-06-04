package withermite.exercise_tracker_api.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public String many() {
        return "many";
    }

    @PostMapping
    public User add(@RequestBody User user) {
        return usersService.create(user);
    }

    @GetMapping("/{key}")
    public User one(@PathVariable String key) {
        return usersService.getOne(key);
    }

    @PutMapping("/{key}")
    public String update(@PathVariable String key) {
        return "update" + key;
    }

    @DeleteMapping("/{key}")
    public String delete(@PathVariable String key) {
        return "delete" + key;
    }
}
