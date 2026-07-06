package withermite.exercise_tracker_api.exercise_type;

import java.util.List;

import org.springframework.stereotype.Service;

import withermite.exercise_tracker_api._util.ResourceWrapper;
import withermite.exercise_tracker_api._util.crud_behaviors.CrudService;

@Service
public class ExerciseTypesService implements CrudService<ExerciseType> {

    private final ExerciseTypesRepository exerciseTypesRepository;

    public ExerciseTypesService(ExerciseTypesRepository exerciseTypesRepository) {
        this.exerciseTypesRepository = exerciseTypesRepository;
    }

    @Override
    public ExerciseType create(ExerciseType exerciseType) {
        return exerciseTypesRepository.save(exerciseType);
    }

    @Override
    public ExerciseType findOne(String name) {
        return exerciseTypesRepository.getOne(name);
    }

    @Override
    public List<ExerciseType> findMany(int pageSize, int offset) {
        return exerciseTypesRepository.getMany(pageSize, offset);
    }

    @Override
    public ResourceWrapper<ExerciseType> replace(String name, ExerciseType exerciseType) {
        return exerciseTypesRepository.replace(name, exerciseType);
    }

    @Override
    public ExerciseType update(String name, ExerciseType exerciseType) {
        return exerciseTypesRepository.update(name, exerciseType);
    }

    @Override
    public boolean delete(String name) {
        return exerciseTypesRepository.delete(name);
    }

}
