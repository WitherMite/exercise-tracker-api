package withermite.exercise_tracker_api.user_workout;

import org.jooq.DSLContext;
import org.jooq.RecordUnmapper;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.MappingException;
import static org.jooq.generated.tables.UserWorkout.USER_WORKOUT;
import org.jooq.generated.tables.records.UserWorkoutRecord;

import withermite.exercise_tracker_api._util.crud_behaviors.EntityMerger;

public class WorkoutUnmapper
        implements EntityMerger<Workout, UserWorkoutRecord>, RecordUnmapper<Workout, UserWorkoutRecord> {

    private final DSLContext create;

    public WorkoutUnmapper(DSLContext create) {
        this.create = create;
    }

    @Override
    public void unmapDiff(Workout workout, UserWorkoutRecord record) {
        if (record == null) {
            return;
        }

        // set values on record to any non null values in dto
    }

    @Override
    public UserWorkoutRecord unmap(Workout workout) throws MappingException {
        try {
            UserWorkoutRecord record = create.newRecord(USER_WORKOUT);
            // map dto to record
            return record;

        } catch (DataAccessException e) {
            // hands off to global handler if cant find needed parents or children
            throw e;

        } catch (Exception e) {
            System.err.println(e.toString());
            throw new MappingException("Exercise could not be mapped to jooq record", e);
        }
    }
}
