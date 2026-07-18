package withermite.exercise_tracker_api.user_workout;

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
@RequestMapping("/users/{userUsername}/workouts")
class WorkoutsController {
    private final String resourceUri = "/users/{userUsername}/workouts";
    private final CrudControllerBehavior<Workout, WorkoutsService, Integer> crud;

    public WorkoutsController(WorkoutsService workoutsService,
            @Value("${userWorkout.defaultPageSize}") int defaultPageSize) {
        this.crud = new CrudControllerBehavior<>(workoutsService, this.resourceUri, defaultPageSize);
    }

    @GetMapping
    public ResponseEntity<List<Workout>> getMany(@Valid PaginationParams params) {
        return crud.getMany(params);
    }

    @PostMapping
    public ResponseEntity<Workout> create(
            @PathVariable String userUsername,
            @RequestBody @Validated(Full.class) Workout workout) {

        workout.userUsername = userUsername;
        return crud.create(workout);
    }

    @GetMapping("/{key}")
    public ResponseEntity<Workout> getOne(@PathVariable Integer key) {
        return crud.getOne(key);
    }

    @PutMapping("/{key}")
    public ResponseEntity<Workout> replace(
            @PathVariable Integer key,
            @PathVariable String userUsername,
            @RequestBody @Validated(Full.class) Workout workout) {

        workout.userUsername = userUsername;
        workout.setId(key);
        return crud.replace(key, workout);
    }

    @PatchMapping("/{key}")
    public ResponseEntity<Workout> update(
            @PathVariable Integer key,
            @PathVariable String userUsername,
            @RequestBody @Validated(AsDelta.class) Workout workout) {

        workout.userUsername = userUsername;
        workout.setId(key);
        return crud.update(key, workout);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable Integer key) {
        return crud.delete(key);
    }
}
