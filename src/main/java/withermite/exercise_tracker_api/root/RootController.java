package withermite.exercise_tracker_api.root;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
class RootController {
    
    @GetMapping
    public String respond() {
        return "hi :3";
    }
}
