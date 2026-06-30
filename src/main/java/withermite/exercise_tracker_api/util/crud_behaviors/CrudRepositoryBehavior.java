package withermite.exercise_tracker_api.util.crud_behaviors;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;

import withermite.exercise_tracker_api.util.EntityMerger;
import withermite.exercise_tracker_api.util.ResourceWrapper;

public class CrudRepositoryBehavior<E extends Entity, R extends UpdatableRecord<R>, KeyType> {
    private final DSLContext create;
    private final Class<E> entityType;
    private final Table<R> table;
    private final TableField<R, KeyType> tableKey;
    private final EntityMerger<E, R> unmapper;

    public CrudRepositoryBehavior(
            DSLContext dslContext, Table<R> table, TableField<R, KeyType> tableKey,
            Class<E> entityType, EntityMerger<E, R> unmapper) {

        this.create = dslContext;
        this.table = table;
        this.tableKey = tableKey;
        this.entityType = entityType;
        this.unmapper = unmapper;
    }

    public E save(E entity) {
        R record = create.newRecord(table, entity);
        record.store();
        return record.into(entityType);
    }

    public E getOne(KeyType key) {
        R record = create.fetchOne(
                table, tableKey.eq(key));

        if (record != null) {
            return record.into(entityType);
        }
        return null;
    }

    public List<E> getMany(int pageSize, int offset) {
        List<E> entities = create.selectFrom(table).limit(pageSize).offset(offset).fetchInto(entityType);
        return entities;
    }

    public E update(KeyType key, E entity) {
        try {
            R record = create.fetchOne(
                    table, tableKey.eq(key));

            if (record != null) {
                unmapper.unmapDiff(entity, record);
                record.update();
                return record.into(entityType);
            }
            return null;

        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public ResourceWrapper<E> replace(KeyType key, E entity) {
        try {
            // try to get from db
            R record = create.fetchOne(
                    table, tableKey.eq(key));

            // if was in db, replace all values, and update
            if (record != null) {
                record.from(entity);
                record.update();
                return new ResourceWrapper<>(record.into(entityType));
            }
            // if not in db create new
            return new ResourceWrapper<>(save(entity), true);

        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean delete(KeyType key) {
        R record = create.fetchOne(
                table, tableKey.eq(key));

        if (record == null) {
            return false;
        }
        record.delete();
        return true;
    }
}
