package withermite.exercise_tracker_api._util.validation.validators;

import org.jooq.EnumType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import withermite.exercise_tracker_api._util.validation.constraints.IsEnumType;

public class IsEnumTypeValidator implements ConstraintValidator<IsEnumType, String> {
    private EnumType[] acceptedValues;

    @Override
    public void initialize(IsEnumType constraintAnnotation) {
        this.acceptedValues = constraintAnnotation.enumTypeClass().getEnumConstants();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return true;

        for (EnumType acceptedValue : acceptedValues) {
            if (value.equals(acceptedValue.getLiteral())) {
                return true;
            }
        }
        return false;
    }

}
