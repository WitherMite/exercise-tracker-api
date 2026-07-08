package withermite.exercise_tracker_api._util.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import withermite.exercise_tracker_api._util.validation.validators.NotBlankIfExistsValidator;

@Constraint(validatedBy = NotBlankIfExistsValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankIfExists {
    String message() default "String must not be blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
