package withermite.exercise_tracker_api.user_workout;

import org.jooq.DSLContext;
import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;
import static org.jooq.generated.tables.UserWorkoutStatistic.USER_WORKOUT_STATISTIC;
import org.jooq.generated.tables.records.UserWorkoutStatisticRecord;
import org.jooq.types.DayToSecond;

import withermite.exercise_tracker_api._util.crud_behaviors.EntityMerger;

public class WorkoutStatisticUnmapper
        implements EntityMerger<WorkoutStatistic, UserWorkoutStatisticRecord>,
        RecordUnmapper<WorkoutStatistic, UserWorkoutStatisticRecord> {

    private final DSLContext create;

    public WorkoutStatisticUnmapper(DSLContext create) {
        this.create = create;
    }

    @Override
    public void unmapDiff(WorkoutStatistic workoutStatistic, UserWorkoutStatisticRecord record) {
        if (record == null)
            return;

        if (workoutStatistic.load != null)
            record.setLoad(workoutStatistic.load);

        if (workoutStatistic.workTime != null)
            record.setWorkTime(workoutStatistic.workTime);

        if (workoutStatistic.restLength != null)
            record.setRestLength(DayToSecond.valueOf(workoutStatistic.restLength));

        if (workoutStatistic.subjectiveEffortValue != null)
            record.setSubjectiveEffortValue(workoutStatistic.subjectiveEffortValue);

    }

    @Override
    public UserWorkoutStatisticRecord unmap(WorkoutStatistic workoutStatistic) throws MappingException {
        try {
            UserWorkoutStatisticRecord record = create.newRecord(USER_WORKOUT_STATISTIC);
            record.setUserWorkoutId(workoutStatistic.workoutId);
            record.setIndex(workoutStatistic.index);
            record.setLoad(workoutStatistic.load);
            record.setWorkTime(workoutStatistic.workTime);
            record.setRestLength(DayToSecond.valueOf(workoutStatistic.restLength));
            record.setSubjectiveEffortValue(workoutStatistic.subjectiveEffortValue);
            return record;
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new MappingException("Exercise could not be mapped to jooq record", e);
        }
    }
}
