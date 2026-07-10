package withermite.exercise_tracker_api.exercise;

import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;
import org.jooq.generated.tables.records.ExerciseRecord;

import withermite.exercise_tracker_api._util.crud_behaviors.EntityMerger;

public class ExerciseUnmapper
        implements EntityMerger<Exercise, ExerciseRecord>, RecordUnmapper<Exercise, ExerciseRecord> {

    @Override
    public void unmapDiff(Exercise exercise, ExerciseRecord record) {
        if (record == null)
            return;

        if (exercise.name != null && !record.getExerciseName().equals(exercise.name))
            record.setExerciseName(exercise.name);

        if (exercise.description != null && !record.getDescription().equals(exercise.description))
            record.setDescription(exercise.description);

    }

    @Override
    public ExerciseRecord unmap(Exercise exercise) throws MappingException {
        try {
            ExerciseRecord record = new ExerciseRecord();
            record.setExerciseName(exercise.name);
            record.setDescription(exercise.description);
            return record;
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new MappingException("user could not be mapped to jooq record", e);
        }
    }
}
