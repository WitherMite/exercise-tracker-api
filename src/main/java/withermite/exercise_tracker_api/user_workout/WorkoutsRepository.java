package withermite.exercise_tracker_api.user_workout;

import java.util.List;

import org.jooq.DSLContext;
import static org.jooq.generated.tables.UserWorkout.USER_WORKOUT;
import org.jooq.generated.tables.records.UserWorkoutRecord;
import org.springframework.stereotype.Repository;

import withermite.exercise_tracker_api._util.ResourceWrapper;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudRepository;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudRepositoryBehavior;

@Repository
public class WorkoutsRepository implements CrudRepository<Workout, Integer> {
    private final CrudRepositoryBehavior<Workout, UserWorkoutRecord, Integer> crud;

    public WorkoutsRepository(DSLContext dslContext) {
        this.crud = new CrudRepositoryBehavior<>(
                dslContext, USER_WORKOUT, USER_WORKOUT.ID,
                Workout.class, new WorkoutUnmapper(dslContext));
    }

    @Override
    public Workout save(Workout entity) {
        return crud.save(entity);
    }

    @Override
    public Workout getOne(Integer key) {
        return crud.getOne(key);
    }

    @Override
    public List<Workout> getMany(int pageSize, int offset) {
        return crud.getMany(pageSize, offset);
    }

    @Override
    public Workout update(Integer key, Workout entity) {
        return crud.update(key, entity);
    }

    @Override
    public ResourceWrapper<Workout> replace(Integer key, Workout entity) {
        return crud.replace(key, entity);
    }

    @Override
    public void delete(Integer key) {
        crud.delete(key);
    }
}
