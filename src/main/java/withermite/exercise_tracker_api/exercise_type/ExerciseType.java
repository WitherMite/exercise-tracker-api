package withermite.exercise_tracker_api.exercise_type;

import java.util.Map;

import org.jooq.generated.enums.CountTypeEnum;
import org.jooq.generated.enums.LoadTypeEnum;
import org.jooq.generated.enums.RestTypeEnum;
import org.jooq.generated.enums.WorkTimeTypeEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import withermite.exercise_tracker_api._util.crud_behaviors.Entity;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.AsDelta;
import withermite.exercise_tracker_api._util.validation.ValidationGroups.Full;
import withermite.exercise_tracker_api._util.validation.constraints.IsEnumType;
import withermite.exercise_tracker_api._util.validation.constraints.NotBlankIfExists;

public class ExerciseType implements Entity {

    @NotBlank(message = "Name must not be blank", groups = Full.class)
    @NotBlankIfExists(message = "Name must not be blank", groups = AsDelta.class)
    public String name;

    @NotNull(message = "Must define a countType", groups = Full.class)
    @IsEnumType(enumTypeClass = CountTypeEnum.class, message = "Count type must be a valid count type")
    public String countType;

    @NotNull(message = "Must define a loadType", groups = Full.class)
    @IsEnumType(enumTypeClass = LoadTypeEnum.class, message = "Load type must be a valid load type")
    public String loadType;

    @NotNull(message = "Must define a workTimeType", groups = Full.class)
    @IsEnumType(enumTypeClass = WorkTimeTypeEnum.class, message = "Work time type must be a valid work time type")
    public String workTimeType;

    @IsEnumType(enumTypeClass = RestTypeEnum.class, message = "Rest type must be a valid rest type")
    public String restType;

    @Override
    public Map<String, String> fetchUriKeys() {
        return Map.of("key", name);
    }

}
