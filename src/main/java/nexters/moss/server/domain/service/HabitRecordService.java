package nexters.moss.server.domain.service;

import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.value.HabitStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HabitRecordService {
    public List<HabitRecord> createHabitRecords(User user, Habit habit) {
        LocalDateTime date = LocalDate.now().minusDays(1).atTime(0, 0, 0);
        List<HabitRecord> habitRecords = new ArrayList<>();
        for(int day = 0 ; day < 5 ; day++) {
            HabitStatus habitStatus = HabitStatus.NOT_DONE;
            // 당일을 기점으로 3일 뒤에는 cake를 받을 수 있음.
            if(day == 3) {
                habitStatus = HabitStatus.CAKE_NOT_DONE;
            }
            habitRecords.add(
                    HabitRecord.builder()
                            .user(user)
                            .habit(habit)
                            .habitStatus(habitStatus)
                            .date(date.plusDays(day))
                            .build()
            );
        }
        return  habitRecords;
    }

    // TODO
    public List<HabitRecord> validateAndRefreshHabitHistory(List<HabitRecord> habitRecords) {
        /*
        1. check if size 5
        2. check if date is sorted by habikery (-1 0 1 2 3)
        3. refresh habit status
        4. if refreshed, reflected in db
         */
        return habitRecords;
    }
}
