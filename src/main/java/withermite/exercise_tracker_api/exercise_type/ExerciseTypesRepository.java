package withermite.exercise_tracker_api.exercise_type;

import java.util.List;

import org.jooq.DSLContext;
import static org.jooq.generated.tables.ExerciseType.EXERCISE_TYPE;
import org.jooq.generated.tables.records.ExerciseTypeRecord;
import org.springframework.stereotype.Repository;

import withermite.exercise_tracker_api._util.ResourceWrapper;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudRepositoryBehavior;

@Repository
public class ExerciseTypesRepository {
    private final CrudRepositoryBehavior<ExerciseType, ExerciseTypeRecord, String> crud;

    public ExerciseTypesRepository(DSLContext dslContext) {
        this.crud = new CrudRepositoryBehavior<>(
                dslContext, EXERCISE_TYPE, EXERCISE_TYPE.EXERCISE_TYPE_NAME,
                ExerciseType.class, new ExerciseTypeUnmapper());
    }

    public ExerciseType save(ExerciseType exerciseType) {
        return crud.save(exerciseType);
    }

    public ExerciseType getOne(String name) {
        return crud.getOne(name);
    }

    public List<ExerciseType> getMany(int pageSize, int offset) {
        return crud.getMany(pageSize, offset);
    }

    public ExerciseType update(String name, ExerciseType exerciseType) {
        return crud.update(name, exerciseType);
    }

    public ResourceWrapper<ExerciseType> replace(String name, ExerciseType exerciseType) {
        return crud.replace(name, exerciseType);
    }

    public boolean delete(String name) {
        return crud.delete(name);
    }
}
