package withermite.exercise_tracker_api.util;

import java.util.List;

public class ResourceWrapper<R> {
    public final boolean wasCreated;
    public final R resource;
    public final List<String> problems;

    public ResourceWrapper(R resource) {
        this.resource = resource;
        this.wasCreated = false;
        this.problems = null;
    }

    public ResourceWrapper(R resource, boolean wasCreated) {
        this.resource = resource;
        this.wasCreated = wasCreated;
        this.problems = null;
    }

    public ResourceWrapper(R resource, List<String> problems) {
        this.resource = resource;
        this.problems = problems;
        this.wasCreated = false;
    }
}
