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
        for (int day = 0; day < 5; day++) {
            HabitStatus habitStatus = HabitStatus.NOT_DONE;
            // 당일을 기점으로 3일 뒤에는 cake를 받을 수 있음.
            if (day == 3) {
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
        return habitRecords;
    }

    public boolean refreshHabitHistoryAndReturnIsChanged(List<HabitRecord> habitRecords) {
        if(habitRecords.size() != 5) {
            // TODO: throw exception
        }
        // TODO: check sorted right
        int nowDay = LocalDateTime.now().getDayOfMonth();
        int recordCurrentDay = habitRecords.get(1).getDate().getDayOfMonth();
        HabitStatus currentHabitStatus = habitRecords.get(1).getHabitStatus();

        if (nowDay != recordCurrentDay) {
            switch (currentHabitStatus) {
                case CAKE_NOT_DONE:
                case NOT_DONE:
                    resetHabitRecords(habitRecords);
                    break;
                case DONE:
                case CAKE_DONE:
                    refreshHabitRecords(habitRecords);
                    break;
            }
            return true;
        }

        return false;
    }

    public void doneHabitRecord(HabitRecord habitRecord) {
        switch (habitRecord.getHabitStatus()) {
            case NOT_DONE:
                habitRecord.switchHabitStatus(HabitStatus.DONE);
                break;
            case CAKE_NOT_DONE:
                habitRecord.switchHabitStatus(HabitStatus.CAKE_DONE);
                break;
        }
    }

    private void resetHabitRecords(List<HabitRecord> habitRecords) {
        for (int day = 0; day < 5; day++) {
            HabitStatus habitStatus = HabitStatus.NOT_DONE;
            if (day == 3) {
                habitStatus = HabitStatus.CAKE_NOT_DONE;
            }

            if (day == 4) {
                habitRecords.get(day).setDate(
                        habitRecords.get(day).getDate().plusDays(1)
                );
                habitRecords.get(day).setHabitStatus(habitStatus);
                continue;
            }

            habitRecords.get(day).setDate(
                    habitRecords.get(day + 1).getDate()
            );
            habitRecords.get(day).setHabitStatus(habitStatus);
        }
    }

    private void refreshHabitRecords(List<HabitRecord> habitRecords) {
        HabitStatus yesterdayHabitStatus = habitRecords.get(0).getHabitStatus();
        HabitRecord lastHabitRecord = habitRecords.get(4);
        for (int index = 0; index < 5; index++) {
            if (index == 4) {
                if(yesterdayHabitStatus == HabitStatus.DONE) {
                    if (lastHabitRecord.getHabitStatus() == HabitStatus.CAKE_NOT_DONE) {
                        lastHabitRecord.setHabitStatus(HabitStatus.NOT_DONE);
                    } else if (lastHabitRecord.getHabitStatus() == HabitStatus.NOT_DONE) {
                        lastHabitRecord.setHabitStatus(HabitStatus.CAKE_NOT_DONE);
                    }
                }

                habitRecords.get(index).setDate(
                        habitRecords.get(index).getDate().plusDays(1)
                );
                continue;
            }
            habitRecords.get(index).changeContents(
                    habitRecords.get(index + 1)
            );
        }
    }
}
