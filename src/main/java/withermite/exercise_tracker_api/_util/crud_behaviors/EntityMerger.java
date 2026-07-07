package withermite.exercise_tracker_api._util.crud_behaviors;

import org.jooq.UpdatableRecord;

public interface EntityMerger<E extends Entity<?>, R extends UpdatableRecord<R>> {
    public void unmapDiff(E entity, R record);
}
