package withermite.exercise_tracker_api.exercise;

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
@RequestMapping("/exercise-types/{exercise-type-name}/exercises")
class ExercisesController {
    private final String resourceUri = "/exercise-types/{exercise-type-name}/exercises";
    private final CrudControllerBehavior<Exercise, ExercisesService, String> crud;

    public ExercisesController(ExercisesService exercisesService,
            @Value("${exercise.defaultPageSize}") int defaultPageSize) {
        this.crud = new CrudControllerBehavior<>(exercisesService, this.resourceUri, defaultPageSize);
    }

    @GetMapping
    public ResponseEntity<List<Exercise>> getMany(@Valid PaginationParams params) {
        return crud.getMany(params);
    }

    @PostMapping
    public ResponseEntity<Exercise> create(@Validated(Full.class) @RequestBody Exercise exercise) {
        return crud.create(exercise);
    }

    @GetMapping("/{key}")
    public ResponseEntity<Exercise> getOne(@PathVariable String key) {
        return crud.getOne(key);
    }

    @PutMapping("/{key}")
    public ResponseEntity<Exercise> replace(@PathVariable String key,
            @Validated(Full.class) @RequestBody @Valid Exercise exercise) {
        return crud.replace(key, exercise);
    }

    @PatchMapping("/{key}")
    public ResponseEntity<Exercise> update(@PathVariable String key,
            @Validated(AsDelta.class) @RequestBody Exercise exercise) {
        return crud.update(key, exercise);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable String key) {
        return crud.delete(key);
    }
}
