package withermite.exercise_tracker_api._util.crud_behaviors;

import java.util.List;

import withermite.exercise_tracker_api._util.ResourceWrapper;

public interface CrudService<E, T> {
    public E create(E entity);

    public E findOne(T key);

    public List<E> findMany(int pageSize, int offset);

    public ResourceWrapper<E> replace(T key, E entity);

    public E update(T key, E entity);

    public void delete(T key);
}
