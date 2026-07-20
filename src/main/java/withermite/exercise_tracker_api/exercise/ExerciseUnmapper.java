package withermite.exercise_tracker_api.exercise;

import org.jooq.DSLContext;
import org.jooq.RecordUnmapper;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.MappingException;
import static org.jooq.generated.tables.Exercise.EXERCISE;
import static org.jooq.generated.tables.ExerciseType.EXERCISE_TYPE;
import org.jooq.generated.tables.records.ExerciseRecord;

import withermite.exercise_tracker_api._util.crud_behaviors.EntityMerger;

public class ExerciseUnmapper
        implements EntityMerger<Exercise, ExerciseRecord>, RecordUnmapper<Exercise, ExerciseRecord> {
    private final DSLContext create;

    public ExerciseUnmapper(DSLContext create) {
        this.create = create;
    }

    @Override
    public void unmapDiff(Exercise exercise, ExerciseRecord record) {
        if (record == null)
            return;

        if (exercise.name != null)
            record.setExerciseName(exercise.name);

        if (exercise.description != null)
            record.setDescription(exercise.description);

    }

    @Override
    public ExerciseRecord unmap(Exercise exercise) throws MappingException {
        try {
            ExerciseRecord record = create.newRecord(EXERCISE);
            record.setExerciseName(exercise.name);
            record.setDescription(exercise.description);
            record.setExerciseTypeId(
                    create.select(EXERCISE_TYPE.ID)
                            .from(EXERCISE_TYPE)
                            .where(EXERCISE_TYPE.EXERCISE_TYPE_NAME.eq(exercise.exerciseType.name))
                            .fetchOne(EXERCISE_TYPE.ID, Integer.class));
            return record;
        } catch (DataAccessException e) {
            // hands off to global handler if cant find an exerciseType parent
            throw e;
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new MappingException("Exercise could not be mapped to jooq record", e);
        }
    }
}
