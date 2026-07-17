package withermite.exercise_tracker_api.user_workout;

import java.util.List;

import org.springframework.stereotype.Service;

import withermite.exercise_tracker_api._util.ResourceWrapper;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudService;

@Service
public class WorkoutsService implements CrudService<Workout, Integer> {
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
    public Workout findOne(Integer key) {
        return workoutsRepository.getOne(key);
    }

    @Override
    public List<Workout> findMany(int pageSize, int offset) {
        return workoutsRepository.getMany(pageSize, offset);
    }

    @Override
    public ResourceWrapper<Workout> replace(Integer key, Workout workout) {
        if (workout.count == null) {
            workout.count = ((short) workout.statistics.size());
        }
        return workoutsRepository.replace(key, workout);
    }

    @Override
    public Workout update(Integer key, Workout workout) {
        if (workout.count == null) {
            workout.count = ((short) workout.statistics.size());
        }
        return workoutsRepository.update(key, workout);
    }

    @Override
    public void delete(Integer key) {
        workoutsRepository.delete(key);
    }
}
