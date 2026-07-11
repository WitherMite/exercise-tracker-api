package withermite.exercise_tracker_api._util.crud_behaviors;

import java.util.List;

import withermite.exercise_tracker_api._util.ResourceWrapper;

public interface CrudService<E> {
    public E create(E entity);

    public E findOne(String key);

    public List<E> findMany(int pageSize, int offset);

    public ResourceWrapper<E> replace(String key, E entity);

    public E update(String key, E entity);

    public void delete(String key);
}
