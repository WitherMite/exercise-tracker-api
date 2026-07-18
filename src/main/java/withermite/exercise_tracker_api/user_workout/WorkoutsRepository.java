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
    private final WorkoutStatisticUnmapper statUnmapper;
    private final CrudRepositoryBehavior<Workout, UserWorkoutRecord, Integer> crud;

    public WorkoutsRepository(DSLContext dslContext) {
        this.create = dslContext;
        this.statUnmapper = new WorkoutStatisticUnmapper(dslContext);
        this.crud = new CrudRepositoryBehavior<>(
                dslContext, USER_WORKOUT, USER_WORKOUT.ID,
                Workout.class, new WorkoutUnmapper(dslContext));
    }

    @Override
    public Workout save(Workout workout) {
        Workout workoutResult = crud.save(workout);

        if (workout.count == 0) {
            return workoutResult;
        }
        for (short i = 0; i < workout.statistics.size(); i++) {
            WorkoutStatistic stat = workout.statistics.get(i);
            stat.workoutId = workoutResult.getId();
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
    @SuppressWarnings("null") // thinks workout.statistics can never be null, doesnt see validation groups
    public Workout update(Integer key, Workout workout) {
        Workout workoutResult = crud.update(key, workout);
        if (workout.statistics == null) {
            return workoutResult;
        }

        for (short i = 0; i < workout.statistics.size(); i++) {
            WorkoutStatistic stat = workout.statistics.get(i);
            // dont change if no provided difference
            if (stat == null)
                continue;

            stat.workoutId = workoutResult.getId();
            stat.index = i;

            UserWorkoutStatisticRecord statRecord = create.fetchOne(
                    USER_WORKOUT_STATISTIC,
                    USER_WORKOUT_STATISTIC.USER_WORKOUT_ID.eq(stat.workoutId)
                            .and(USER_WORKOUT_STATISTIC.INDEX.eq(stat.index)));
            // update or do nothing
            if (statRecord == null)
                continue;

            statRecord.from(stat);
            statRecord.update();

            if (workoutResult.statistics.get(i) != null) {
                workoutResult.statistics.remove(i);
            }
            workoutResult.statistics.add(i, statRecord.into(WorkoutStatistic.class));
        }

        return workoutResult;
    }

    @Override
    public ResourceWrapper<Workout> replace(Integer key, Workout workout) {

        ResourceWrapper<Workout> workoutResult = crud.replace(key, workout);

        for (short i = 0; i < workout.statistics.size(); i++) {
            WorkoutStatistic stat = workout.statistics.get(i);
            if (stat == null) {
                create.deleteFrom(USER_WORKOUT_STATISTIC)
                        .where(USER_WORKOUT_STATISTIC.USER_WORKOUT_ID.eq(workoutResult.resource.getId())
                                .and(USER_WORKOUT_STATISTIC.INDEX.eq(i)))
                        .execute();
                continue;
            }

            stat.workoutId = workoutResult.resource.getId();
            stat.index = i;

            UserWorkoutStatisticRecord statRecord = create.fetchOne(
                    USER_WORKOUT_STATISTIC,
                    USER_WORKOUT_STATISTIC.USER_WORKOUT_ID.eq(stat.workoutId)
                            .and(USER_WORKOUT_STATISTIC.INDEX.eq(stat.index)));
            // create or replace
            if (statRecord == null) {
                statRecord = create.newRecord(USER_WORKOUT_STATISTIC, stat);
                statRecord.store();
            } else {
                statUnmapper.unmapDiff(stat, statRecord);
                statRecord.update();
                workoutResult.resource.statistics.remove(i);
            }

            workoutResult.resource.statistics.add(i, statRecord.into(WorkoutStatistic.class));
        }

        return workoutResult;
    }

    @Override
    public void delete(Integer key) {
        crud.delete(key);
    }
}
