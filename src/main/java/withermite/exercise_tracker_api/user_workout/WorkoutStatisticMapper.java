package withermite.exercise_tracker_api.user_workout;

import org.jooq.RecordMapper;
import org.jooq.generated.tables.records.UserWorkoutStatisticRecord;

public class WorkoutStatisticMapper implements RecordMapper<UserWorkoutStatisticRecord, WorkoutStatistic> {

    @Override
    public WorkoutStatistic map(UserWorkoutStatisticRecord record) {
        WorkoutStatistic statistic = new WorkoutStatistic();

        statistic.load = record.getLoad();
        statistic.workTime = record.getWorkTime();
        statistic.subjectiveEffortValue = record.getSubjectiveEffortValue();
        var rest = record.getRestLength();
        if (rest != null) {
            statistic.restLength = rest.toDuration();
        }

        return statistic;
    }
}
