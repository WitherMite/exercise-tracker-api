package withermite.exercise_tracker_api.exercise_type;

import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;
import org.jooq.generated.enums.CountTypeEnum;
import org.jooq.generated.enums.LoadTypeEnum;
import org.jooq.generated.enums.RestTypeEnum;
import org.jooq.generated.enums.WorkTimeTypeEnum;
import org.jooq.generated.tables.records.ExerciseTypeRecord;

import withermite.exercise_tracker_api._util.EntityMerger;

public class ExerciseTypeUnmapper
        implements EntityMerger<ExerciseType, ExerciseTypeRecord>, RecordUnmapper<ExerciseType, ExerciseTypeRecord> {

    @Override
    @SuppressWarnings("null") // thinks fields can never be null, when that is not true, remove while changing
                              // this method to catch other null warnings
    public void unmapDiff(ExerciseType exerciseType, ExerciseTypeRecord record) {
        if (record == null)
            return;

        if (exerciseType.name != null && !record.getExerciseTypeName().equals(exerciseType.name))
            record.setExerciseTypeName(exerciseType.name);

        if (exerciseType.countType != null && !record.getCountType().getLiteral().equals(exerciseType.countType))
            record.setCountType(CountTypeEnum.lookupLiteral(exerciseType.countType));

        if (exerciseType.loadType != null && !record.getLoadType().getLiteral().equals(exerciseType.loadType))
            record.setLoadType(LoadTypeEnum.lookupLiteral(exerciseType.loadType));

        if (exerciseType.workTimeType != null
                && !record.getWorkTimeType().getLiteral().equals(exerciseType.workTimeType))
            record.setWorkTimeType(WorkTimeTypeEnum.lookupLiteral(exerciseType.workTimeType));

        if (exerciseType.restType != null && !record.getRestType().getLiteral().equals(exerciseType.restType))
            record.setRestType(RestTypeEnum.lookupLiteral(exerciseType.restType));
    }

    @Override
    public ExerciseTypeRecord unmap(ExerciseType exerciseType) throws MappingException {
        try {
            ExerciseTypeRecord record = new ExerciseTypeRecord();
            record.setExerciseTypeName(exerciseType.name);
            record.setCountType(CountTypeEnum.lookupLiteral(exerciseType.countType));
            record.setLoadType(LoadTypeEnum.lookupLiteral(exerciseType.loadType));
            record.setWorkTimeType(WorkTimeTypeEnum.lookupLiteral(exerciseType.workTimeType));
            if (exerciseType.restType == null) {
                record.setRestType(RestTypeEnum.lookupLiteral("optional"));
            } else {
                record.setRestType(RestTypeEnum.lookupLiteral(exerciseType.restType));
            }
            return record;
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new MappingException("user could not be mapped to jooq record", e);
        }
    }
}
