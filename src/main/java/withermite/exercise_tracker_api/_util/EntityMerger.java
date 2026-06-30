package withermite.exercise_tracker_api._util;

import org.jooq.UpdatableRecord;

import withermite.exercise_tracker_api._util.crud_behaviors.Entity;

public interface EntityMerger<E extends Entity<?>, R extends UpdatableRecord<R>> {
    public void unmapDiff(E entity, R record);
}
