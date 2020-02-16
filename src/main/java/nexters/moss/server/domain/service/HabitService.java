package nexters.moss.server.domain.service;

import nexters.moss.server.domain.model.Habit;
import org.springframework.stereotype.Service;

@Service
public class HabitService {
    private HabitRecordService habitRecordService;

    public HabitService(HabitRecordService habitRecordService) {
        this.habitRecordService = habitRecordService;
    }

    public void doDoneHabit(Habit habit) {
        habit.onFirstCheck();
        habitRecordService.doneHabitRecord(habit.getHabitRecords().get(1));
    }
}
