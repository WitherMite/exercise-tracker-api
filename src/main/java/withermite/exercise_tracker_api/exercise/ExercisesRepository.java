package withermite.exercise_tracker_api.exercise;

import java.util.List;

import org.jooq.DSLContext;
import static org.jooq.generated.tables.Exercise.EXERCISE;
import org.jooq.generated.tables.records.ExerciseRecord;
import org.springframework.stereotype.Repository;

import withermite.exercise_tracker_api._util.ResourceWrapper;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudRepositoryBehavior;

@Repository
public class ExercisesRepository {
    private final CrudRepositoryBehavior<Exercise, ExerciseRecord, String> crud;

    public ExercisesRepository(DSLContext dslContext) {
        this.crud = new CrudRepositoryBehavior<>(
                dslContext, EXERCISE, EXERCISE.EXERCISE_NAME,
                Exercise.class, new ExerciseUnmapper());
    }

    public Exercise save(Exercise exercise) {
        return crud.save(exercise);
    }

    public Exercise getOne(String name) {
        return crud.getOne(name);
    }

    public List<Exercise> getMany(int pageSize, int offset) {
        return crud.getMany(pageSize, offset);
    }

    public Exercise update(String name, Exercise exercise) {
        return crud.update(name, exercise);
    }

    public ResourceWrapper<Exercise> replace(String name, Exercise exercise) {
        return crud.replace(name, exercise);
    }

    public void delete(String name) {
        crud.delete(name);
    }

}
