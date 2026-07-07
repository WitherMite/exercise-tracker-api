package withermite.exercise_tracker_api._util.validation;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

public class ValidationGroups {
    public interface Full {
    }

    public interface AsDelta {
    }

    @GroupSequence({ Default.class, AsDelta.class, Full.class })
    public interface Always {
    }
}
