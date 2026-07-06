package withermite.exercise_tracker_api.exercise_type;

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
@RequestMapping("/exercise-types")
class ExerciseTypesController {

    private final String resourceUri = "/exercise-types";
    private final CrudControllerBehavior<ExerciseType, ExerciseTypesService> crud;

    public ExerciseTypesController(ExerciseTypesService exerciseTypesService,
            @Value("${exerciseType.defaultPageSize}") int defaultPageSize) {
        this.crud = new CrudControllerBehavior<>(exerciseTypesService, this.resourceUri, defaultPageSize);
    }

    @GetMapping
    public ResponseEntity<List<ExerciseType>> getMany(@Valid PaginationParams params) {
        return crud.getMany(params);
    }

    @PostMapping
    public ResponseEntity<ExerciseType> create(@Validated(Full.class) @RequestBody ExerciseType exerciseType) {
        return crud.create(exerciseType);
    }

    @GetMapping("/{key}")
    public ResponseEntity<ExerciseType> getOne(@PathVariable String key) {
        return crud.getOne(key);
    }

    @PutMapping("/{key}")
    public ResponseEntity<ExerciseType> replace(@PathVariable String key,
            @Validated(Full.class) @RequestBody @Valid ExerciseType exerciseType) {
        return crud.replace(key, exerciseType);
    }

    @PatchMapping("/{key}")
    public ResponseEntity<ExerciseType> update(@PathVariable String key,
            @Validated(AsDelta.class) @RequestBody ExerciseType exerciseType) {
        return crud.update(key, exerciseType);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable String key) {
        return crud.delete(key);
    }
}
