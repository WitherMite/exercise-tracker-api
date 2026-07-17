package withermite.exercise_tracker_api.user_workout;

import java.util.List;

import org.jooq.DSLContext;
import static org.jooq.generated.tables.UserWorkout.USER_WORKOUT;
import static org.jooq.generated.tables.UserWorkoutStatistic.USER_WORKOUT_STATISTIC;
import org.jooq.generated.tables.records.UserWorkoutRecord;
import org.jooq.generated.tables.records.UserWorkoutStatisticRecord;
import org.springframework.stereotype.Repository;

import withermite.exercise_tracker_api._util.ResourceWrapper;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudRepository;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudRepositoryBehavior;

@Repository
public class WorkoutsRepository implements CrudRepository<Workout, Integer> {
    private final DSLContext create;
    private final CrudRepositoryBehavior<Workout, UserWorkoutRecord, Integer> crud;

    public WorkoutsRepository(DSLContext dslContext) {
        this.create = dslContext;
        this.crud = new CrudRepositoryBehavior<>(
                dslContext, USER_WORKOUT, USER_WORKOUT.ID,
                Workout.class, new WorkoutUnmapper(dslContext));
    }

    @Override
    public Workout save(Workout workout) {
        Workout workoutResult = crud.save(workout);

        for (short i = 0; i < workout.statistics.size(); i++) {
            WorkoutStatistic stat = workout.statistics.get(i);
            stat.workoutId = workoutResult.id;
            stat.index = i;

            UserWorkoutStatisticRecord statRecord = create.newRecord(USER_WORKOUT_STATISTIC, stat);
            statRecord.store();
            workoutResult.statistics.add(
                    statRecord.getIndex(),
                    statRecord.into(WorkoutStatistic.class));
        }

        return workoutResult;
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
    public Workout update(Integer key, Workout workout) {
        return crud.update(key, workout);
    }

    @Override
    public ResourceWrapper<Workout> replace(Integer key, Workout workout) {
        return crud.replace(key, workout);
    }

    @Override
    public void delete(Integer key) {
        crud.delete(key);
    }
}
