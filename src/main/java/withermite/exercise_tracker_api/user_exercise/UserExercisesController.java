package withermite.exercise_tracker_api.user_exercise;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-exercises")
class UserExercisesController {
    
    @GetMapping
    public String many() {
        return "many";
    }

    @PostMapping
    public String add() {
        return "add";
    }

    @GetMapping("/{key}")
    public String one(@PathVariable Long key) {
        return "" + key;
    }

    @PutMapping("/{key}")
    public String update(@PathVariable Long key) {
        return "update" + key;
    }

    @DeleteMapping("/{key}")
    public String delete(@PathVariable Long key) {
        return "delete" + key;
    }
}
