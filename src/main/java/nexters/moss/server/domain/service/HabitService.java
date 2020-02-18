package nexters.moss.server.domain.service;

import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.HabitRecord;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public boolean isDoneHabit(Habit habit) {
        return habitRecordService.isDoneRecord(habit.getHabitRecords().get(1));
    }
}
