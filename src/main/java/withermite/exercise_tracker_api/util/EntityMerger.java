package withermite.exercise_tracker_api.util;

import org.jooq.UpdatableRecord;

import withermite.exercise_tracker_api.util.crud_behaviors.Entity;

public interface EntityMerger<E extends Entity, R extends UpdatableRecord<R>> {
    public void unmapDiff(E entity, R record);
}
