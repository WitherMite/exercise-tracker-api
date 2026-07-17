package withermite.exercise_tracker_api.user_workout;

import java.util.List;

import org.springframework.stereotype.Service;

import withermite.exercise_tracker_api._util.ResourceWrapper;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudService;

@Service
public class WorkoutsService implements CrudService<Workout> {
    private final WorkoutsRepository workoutsRepository;

    public WorkoutsService(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    @Override
    public Workout create(Workout workout) {
        if (workout.count == null) {
            workout.count = ((short) workout.statistics.size());
        }
        return workoutsRepository.save(workout);
    }

    @Override
    public Workout findOne(String key) {
        Integer intKey = Integer.valueOf(key);
        return workoutsRepository.getOne(intKey);
    }

    @Override
    public List<Workout> findMany(int pageSize, int offset) {
        return workoutsRepository.getMany(pageSize, offset);
    }

    @Override
    public ResourceWrapper<Workout> replace(String key, Workout workout) {
        Integer intKey = Integer.valueOf(key);
        return workoutsRepository.replace(intKey, workout);
    }

    @Override
    public Workout update(String key, Workout workout) {
        Integer intKey = Integer.valueOf(key);
        return workoutsRepository.update(intKey, workout);
    }

    @Override
    public void delete(String key) {
        Integer intKey = Integer.valueOf(key);
        workoutsRepository.delete(intKey);
    }
}
