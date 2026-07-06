package withermite.exercise_tracker_api._util;

public class ResourceWrapper<R> {
    public final boolean wasCreated;
    public final R resource;

    public ResourceWrapper(R resource) {
        this.resource = resource;
        this.wasCreated = false;
    }

    public ResourceWrapper(R resource, boolean wasCreated) {
        this.resource = resource;
        this.wasCreated = wasCreated;
    }
}
