package withermite.exercise_tracker_api._util.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import withermite.exercise_tracker_api._util.validation.constraints.NotBlankIfExists;

public class NotBlankIfExistsValidator implements ConstraintValidator<NotBlankIfExists, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.trim().length() > 0;
    }
}
