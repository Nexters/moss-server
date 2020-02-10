package nexters.moss.server.habit;

import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.HabitRecordRepository;
import nexters.moss.server.domain.repository.HabitRepository;
import nexters.moss.server.domain.repository.UserRepository;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.HabitType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class HabitTypeApplicationServiceTest {
    private User testUser;

    private Habit testHabit;

    @Autowired
    private HabitApplicationService habitApplicationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitRecordRepository habitRecordRepository;

    @Before
    public void setup() {
        testUser = userRepository.save(new User(null, "socialId", "accounToken", "nickName", null, null, null, null));
        testHabit = habitRepository.save(new Habit(null, HabitType.WATER, CakeType.WATERMELON, null, null));
    }

    @Test
    public void getHabitHistoryTest() {
        habitApplicationService.createHabit(testUser.getId(), testHabit.getId());
        Response<List<HabitHistory>> getResponse = habitApplicationService.getHabitHistory(testUser.getId());
        Assert.assertEquals(1, getResponse.getData().size());

        for (HabitHistory habitHistory: getResponse.getData()) {
            Assert.assertEquals(5, habitHistory.getHabitRecords().size());
        }

        return;
    }

    @Test
    public void createHabitTest() {
        Response<HabitHistory> createResponse = habitApplicationService.createHabit(testUser.getId(), testHabit.getId());
        Assert.assertNotNull(createResponse.getData());

        List<HabitRecord> habitRecords = createResponse.getData().getHabitRecords();
        Assert.assertEquals(5, habitRecords.size());
        int nowDay = LocalDateTime.now().minusDays(1).getDayOfMonth();
        for(HabitRecord habitRecord: habitRecords) {
            Assert.assertEquals(nowDay, habitRecord.getDate().getDayOfMonth());
            nowDay++;
        }
    }

    @Test
    public void deleteHabitTest() {
        Response<HabitHistory> deleteResponse = habitApplicationService.createHabit(testUser.getId(), testHabit.getId());
        Assert.assertNotNull(deleteResponse.getData());

        List<HabitRecord> habitRecords = deleteResponse.getData().getHabitRecords();
        Assert.assertEquals(5, habitRecords.size());

        habitApplicationService.deleteHabit(testUser.getId(), testHabit.getId());
        Assert.assertEquals(0, habitRecordRepository.findAllByUserAndHabit(testUser, testHabit).size());
    }
}
