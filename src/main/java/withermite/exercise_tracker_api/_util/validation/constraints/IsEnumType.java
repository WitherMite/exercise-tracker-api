package withermite.exercise_tracker_api._util.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jooq.EnumType;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import withermite.exercise_tracker_api._util.validation.validators.IsEnumTypeValidator;

@Constraint(validatedBy = IsEnumTypeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsEnumType {
    Class<? extends EnumType> enumTypeClass();

    String message() default "String must be one of valid options";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
