package nexters.moss.server.habit;

import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.service.HabitRecordService;
import nexters.moss.server.domain.value.HabitStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static nexters.moss.server.domain.value.HabitStatus.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class HabitRecordServiceTest {
    @Autowired
    HabitRecordService habitRecordService;

    // start  : NOT_DONE / NOT_DONE / NOT_DONE / CAKE_NOT_DONE / NOT_DONE
    // changed: NOT_DONE / NOT_DONE / NOT_DONE / CAKE_NOT_DONE / NOT_DONE
    // end    : NOT_DONE / NOT_DONE / NOT_DONE / CAKE_NOT_DONE / NOT_DONE
    @Test
    public void yesterdayIsNotDoneAndTodayIsNotDoneAndNotChangedAndTodayTest() {
        // given
        LocalDateTime date = LocalDate.now().atTime(0, 0, 0);
        List<HabitRecord> habitRecords = setupDefaultHabitRecordsWithStartDate(date);

        // when
        boolean isChanged = habitRecordService.refreshHabitHistoryAndReturnIsChanged(habitRecords);

        // then
        Assert.assertFalse(isChanged);
        for(int day = 0 ; day < 5 ; day++) {
            if(day == 3) {
                Assert.assertEquals(CAKE_NOT_DONE, habitRecords.get(day).getHabitStatus());
                continue;
            }
            Assert.assertEquals(NOT_DONE, habitRecords.get(day).getHabitStatus());
        }
    }

    // start  : NOT_DONE / NOT_DONE / NOT_DONE / CAKE_NOT_DONE / NOT_DONE
    // changed: NOT_DONE / NOT_DONE / NOT_DONE / CAKE_NOT_DONE / NOT_DONE
    // end    : NOT_DONE / NOT_DONE / NOT_DONE / CAKE_NOT_DONE / NOT_DONE
    @Test
    public void yesterdayIsNotDoneAndTodayIsNotDoneAndNotChangedAndNextDayTest() {
        // given
        LocalDateTime date = LocalDate.now().minusDays(1).atTime(0, 0, 0);
        List<HabitRecord> habitRecords = setupDefaultHabitRecordsWithStartDate(date);

        // when
        boolean isChanged = habitRecordService.refreshHabitHistoryAndReturnIsChanged(habitRecords);

        // then
        Assert.assertTrue(isChanged);
        for(int day = 0 ; day < 5 ; day++) {
            if(day == 3) {
                Assert.assertEquals(CAKE_NOT_DONE, habitRecords.get(day).getHabitStatus());
                continue;
            }
            Assert.assertEquals(NOT_DONE, habitRecords.get(day).getHabitStatus());
        }
    }

    // start  : NOT_DONE / NOT_DONE / NOT_DONE / CAKE_NOT_DONE / NOT_DONE
    // changed: NOT_DONE / DONE / NOT_DONE / CAKE_NOT_DONE / NOT_DONE
    // end    : DONE / NOT_DONE / CAKE_NOT_DONE / NOT_DONE / NOT_DONE
    @Test
    public void yesterdayIsNotDoneAndTodayIsDoneAndChangedAndNextDayTest() {
        // given
        LocalDateTime date = LocalDate.now().minusDays(1).atTime(0, 0, 0);
        List<HabitRecord> habitRecords = setupDefaultHabitRecordsWithStartDate(date);

        // when
        habitRecords.get(1).setHabitStatus(DONE);
        boolean isChanged = habitRecordService.refreshHabitHistoryAndReturnIsChanged(habitRecords);

        // then
        Assert.assertTrue(isChanged);
        for(int day = 0 ; day < 5 ; day++) {
            if(day == 0) {
                Assert.assertEquals(DONE, habitRecords.get(day).getHabitStatus());
                continue;
            }
            if(day == 2) {
                Assert.assertEquals(CAKE_NOT_DONE, habitRecords.get(day).getHabitStatus());
                continue;
            }
            Assert.assertEquals(NOT_DONE, habitRecords.get(day).getHabitStatus());
        }
    }

    // start  : DONE / CAKE_NOT_DONE / NOT_DONE / NOT_DONE / CAKE_NOT_DONE
    // changed: DONE / CAKE_DONE / NOT_DONE / NOT_DONE / CAKE_NOT_DONE
    // end    : CAKE_DONE / NOT_DONE / NOT_DONE / CAKE_NOT_DONE / NOT_DONE
    @Test
    public void yesterdayIsDoneAndTodayIsCakeNotDoneAndChangedAndNextDayTest() {
        // given
        LocalDateTime date = LocalDate.now().minusDays(1).atTime(0, 0, 0);
        List<HabitRecord> habitRecords = setupDefaultHabitRecordsWithStartDate(date);
        habitRecords.get(0).setHabitStatus(DONE);
        habitRecords.get(1).setHabitStatus(CAKE_NOT_DONE);
        habitRecords.get(2).setHabitStatus(NOT_DONE);
        habitRecords.get(3).setHabitStatus(NOT_DONE);
        habitRecords.get(4).setHabitStatus(CAKE_NOT_DONE);

        // when
        habitRecords.get(1).setHabitStatus(CAKE_DONE);
        boolean isChanged = habitRecordService.refreshHabitHistoryAndReturnIsChanged(habitRecords);

        // then
        Assert.assertTrue(isChanged);
        for(int day = 0 ; day < 5 ; day++) {
            if(day == 0) {
                Assert.assertEquals(CAKE_DONE, habitRecords.get(day).getHabitStatus());
                continue;
            }
            if(day == 3) {
                Assert.assertEquals(CAKE_NOT_DONE, habitRecords.get(day).getHabitStatus());
                continue;
            }
            Assert.assertEquals(NOT_DONE, habitRecords.get(day).getHabitStatus());
        }
    }

    // start  : DONE / NOT_DONE / CAKE_NOT_DONE / NOT_DONE / NOT_DONE
    // changed: DONE / DONE / CAKE_NOT_DONE / NOT_DONE / NOT_DONE
    // end    : DONE / CAKE_NOT_DONE / NOT_DONE / NOT_DONE / CAKE_NOT_DONE
    @Test
    public void yesterdayIsDoneAndTodayIsNotDoneAndChangedAndNextDayTest() {
        // given
        LocalDateTime date = LocalDate.now().minusDays(1).atTime(0, 0, 0);
        List<HabitRecord> habitRecords = setupDefaultHabitRecordsWithStartDate(date);
        habitRecords.get(0).setHabitStatus(DONE);
        habitRecords.get(1).setHabitStatus(NOT_DONE);
        habitRecords.get(2).setHabitStatus(CAKE_NOT_DONE);
        habitRecords.get(3).setHabitStatus(NOT_DONE);
        habitRecords.get(4).setHabitStatus(NOT_DONE);

        // when
        habitRecords.get(1).setHabitStatus(DONE);
        boolean isChanged = habitRecordService.refreshHabitHistoryAndReturnIsChanged(habitRecords);

        // then
        Assert.assertTrue(isChanged);
        for(int day = 0 ; day < 5 ; day++) {
            if(day == 0) {
                Assert.assertEquals(DONE, habitRecords.get(day).getHabitStatus());
                continue;
            }
            if(day == 1 || day == 4) {
                Assert.assertEquals(CAKE_NOT_DONE, habitRecords.get(day).getHabitStatus());
                continue;
            }
            Assert.assertEquals(NOT_DONE, habitRecords.get(day).getHabitStatus());
        }
    }

    // parameter 'date' is today based
    private List<HabitRecord> setupDefaultHabitRecordsWithStartDate(LocalDateTime date) {
        date = date.minusDays(1);
        List<HabitRecord> habitRecords = new ArrayList<>();
        for(int day = 0 ; day < 5; day++) {
            HabitStatus habitStatus = NOT_DONE;
            // 당일을 기점으로 3일 뒤에는 cake를 받을 수 있음.
            if (day == 3) {
                habitStatus = HabitStatus.CAKE_NOT_DONE;
            }
            habitRecords.add(
                    HabitRecord.builder()
                            .habitStatus(habitStatus)
                            .date(date.plusDays(day))
                            .build()
            );
        }
        return habitRecords;
    }
}
