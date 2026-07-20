package withermite.exercise_tracker_api._util.crud_behaviors;

import org.jooq.UpdatableRecord;

public interface EntityMerger<E, R extends UpdatableRecord<R>> {
    public void unmapDiff(E entity, R record);
}
