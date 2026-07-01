package withermite.exercise_tracker_api.exercise_type;

import org.jooq.RecordMapper;
import org.jooq.generated.tables.records.ExerciseTypeRecord;

public class ExerciseTypeMapper implements RecordMapper<ExerciseTypeRecord, ExerciseType> {
    @Override
    public ExerciseType map(ExerciseTypeRecord record) {
        ExerciseType exerciseType = new ExerciseType();
        exerciseType.name = record.getExerciseTypeName();
        exerciseType.countType = record.getCountType().getLiteral();
        exerciseType.loadType = record.getLoadType().getLiteral();
        exerciseType.workTimeType = record.getWorkTimeType().getLiteral();
        exerciseType.restType = record.getRestType().getLiteral();
        return exerciseType;
    }
}
