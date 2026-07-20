package withermite.exercise_tracker_api._util.crud_behaviors;

import java.util.List;

import withermite.exercise_tracker_api._util.ResourceWrapper;

public interface CrudRepository<E, T> {
    public E save(E entity);

    public E getOne(T key);

    public List<E> getMany(int pageSize, int offset);

    public E update(T key, E entity);

    public ResourceWrapper<E> replace(T key, E entity);

    public void delete(T key);
}
