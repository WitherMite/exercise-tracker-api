package withermite.exercise_tracker_api.user_workout;

import org.jooq.DSLContext;
import org.jooq.RecordUnmapper;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.MappingException;
import org.jooq.generated.enums.SubjectiveEffortTypeEnum;
import static org.jooq.generated.tables.AppUser.APP_USER;
import static org.jooq.generated.tables.Exercise.EXERCISE;
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

        if (workout.datetime != null && !record.getDatetime().equals(workout.datetime))
            record.setDatetime(workout.datetime);

        if (workout.count != null && !record.getWorkoutCount().equals(workout.count))
            record.setWorkoutCount(workout.count);

        if (workout.notes != null && !record.getNotes().equals(workout.notes))
            record.setNotes(workout.notes);

        if (workout.subjectiveEffortType != null
                && !record.getSubjectiveEffortType().getLiteral().equals(workout.subjectiveEffortType))
            record.setSubjectiveEffortType(SubjectiveEffortTypeEnum.lookupLiteral(workout.subjectiveEffortType));
    }

    @Override
    public UserWorkoutRecord unmap(Workout workout) throws MappingException {
        try {
            UserWorkoutRecord record = create.newRecord(USER_WORKOUT);
            record.setDatetime(workout.datetime);
            record.setWorkoutCount(workout.count);
            record.setNotes(workout.notes);
            record.setSubjectiveEffortType(SubjectiveEffortTypeEnum.lookupLiteral(workout.subjectiveEffortType));

            Integer[] ids = create.select(EXERCISE.ID, APP_USER.ID)
                    .from(EXERCISE, APP_USER)
                    .where(EXERCISE.EXERCISE_NAME.eq(workout.exercise.name)
                            .and(APP_USER.USERNAME.eq(workout.userUsername)))
                    .fetchOneInto(Integer[].class);

            if (ids[0] != null)
                record.setExerciseId(ids[0]);

            if (ids[1] != null)
                record.setUserId(ids[1]);

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
