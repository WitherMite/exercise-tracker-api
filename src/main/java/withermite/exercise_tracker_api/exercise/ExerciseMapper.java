package withermite.exercise_tracker_api.exercise;

import org.jooq.RecordMapper;
import static org.jooq.generated.Keys.EXERCISE__EXERCISE_EXERCISE_TYPE_ID_FKEY;
import org.jooq.generated.tables.records.ExerciseRecord;

import withermite.exercise_tracker_api.exercise_type.ExerciseType;

public class ExerciseMapper implements RecordMapper<ExerciseRecord, Exercise> {
    @Override
    public Exercise map(ExerciseRecord record) {
        Exercise exercise = new Exercise();

        exercise.name = record.getExerciseName();
        exercise.description = record.getDescription();

        ExerciseType exerciseType = record
                .fetchParent(EXERCISE__EXERCISE_EXERCISE_TYPE_ID_FKEY)
                .into(ExerciseType.class);
        exercise.exerciseType = exerciseType;

        return exercise;
    }
}
