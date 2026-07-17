package withermite.exercise_tracker_api.exercise;

import java.util.List;

import org.springframework.stereotype.Service;

import withermite.exercise_tracker_api._util.ResourceWrapper;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudService;

@Service
public class ExercisesService implements CrudService<Exercise, String> {
    private final ExercisesRepository exerciseRepository;

    public ExercisesService(ExercisesRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public Exercise create(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @Override
    public Exercise findOne(String key) {
        return exerciseRepository.getOne(key);
    }

    @Override
    public List<Exercise> findMany(int pageSize, int offset) {
        return exerciseRepository.getMany(pageSize, offset);
    }

    @Override
    public ResourceWrapper<Exercise> replace(String key, Exercise exercise) {
        return exerciseRepository.replace(key, exercise);
    }

    @Override
    public Exercise update(String key, Exercise exercise) {
        return exerciseRepository.update(key, exercise);
    }

    @Override
    public void delete(String key) {
        exerciseRepository.delete(key);
    }

}
