package withermite.exercise_tracker_api.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UsersController {
    
    @GetMapping
    public String many() {
        return "many";
    }

    @PostMapping
    public String add() {
        return "add";
    }

    @GetMapping("/{key}")
    public String one() {
        return "one";
    }

    @PutMapping("/{key}")
    public String update() {
        return "update";
    }

    @DeleteMapping("/{key}")
    public String delete() {
        return "delete";
    }
}
