package nexters.moss.server.habit;

import nexters.moss.server.TestConfiguration;
import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.cake.ReceivedPieceOfCakeRepository;
import nexters.moss.server.domain.cake.WholeCakeRepository;
import nexters.moss.server.domain.habit.HabitRecordRepository;
import nexters.moss.server.domain.habit.HabitRepository;
import nexters.moss.server.domain.Category;
import nexters.moss.server.domain.habit.Habit;
import nexters.moss.server.domain.habit.HabitRecord;
import nexters.moss.server.domain.user.User;
import nexters.moss.server.domain.user.UserRepository;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.HabitType;
import org.junit.After;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class HabitApplicationServiceTest {
    private Category testCategory;

    private User testUser;

    @Autowired
    private TestConfiguration testConfiguration;

    @Autowired
    private HabitApplicationService habitApplicationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitRecordRepository habitRecordRepository;

    private ReceivedPieceOfCakeRepository receivedPieceOfCakeRepository;
    private WholeCakeRepository wholeCakeRepository;

    @Before
    public void setup() {
        testCategory = new Category(1L, HabitType.WATER, CakeType.WATERMELON, null, null);

        testUser = userRepository.save(
                new User(
                        null,
                        12345678L,
                        "accountToken",
                        "nickname",
                        null
                )
        );
    }

    @After
    public void tearDown() {
        testConfiguration.tearDown();
    }

    @Test
    // TODO
    public void getHabitHistoryTest() {
        habitApplicationService.createHabit(testUser.getId(), testCategory.getId());
        Response<List<HabitHistory>> getResponse = habitApplicationService.getHabit(testUser.getId());
        Assert.assertEquals(1, getResponse.getData().size());

        for (HabitHistory habitHistory : getResponse.getData()) {
            Assert.assertEquals(5, habitHistory.getHabitRecords().size());
        }
    }

    @Test
    public void createHabitTest() {
        Response<HabitHistory> createResponse = habitApplicationService.createHabit(testUser.getId(), testCategory.getId());
        Assert.assertNotNull(createResponse.getData());

        List<HabitRecord> habitRecords = createResponse.getData().getHabitRecords();
        Assert.assertEquals(5, habitRecords.size());
        int nowDay = LocalDateTime.now().minusDays(1).getDayOfMonth();
        for (HabitRecord habitRecord : habitRecords) {
            Assert.assertEquals(nowDay, habitRecord.getDate().getDayOfMonth());
            nowDay++;
        }
    }

    @Test
    public void deleteHabitTest() {
        Response<HabitHistory> createResponse = habitApplicationService.createHabit(testUser.getId(), testCategory.getId());
        long habitId = createResponse.getData().getHabitId();
        habitApplicationService.deleteHabit(testUser.getId(), habitId);
        Assert.assertEquals(0, habitRecordRepository.findAllByUserIdAndHabitId(testUser.getId(), habitId).size());
    }

    @Test
    public void doneHabitTest() {
        // given
        receivedPieceOfCakeRepository = mock(ReceivedPieceOfCakeRepository.class);
        wholeCakeRepository = mock(WholeCakeRepository.class);

        given(receivedPieceOfCakeRepository.countAllByUserIdAndCategoryId(testUser.getId(), testCategory.getId()))
                .willReturn(8);

        Response<HabitHistory> createResponse = habitApplicationService.createHabit(testUser.getId(), testCategory.getId());
        long habitId = createResponse.getData().getHabitId();

        // when
        habitApplicationService.doneHabit(testUser.getId(), habitId);

        // then
        Habit habit = habitRepository.findById(habitId).get();
        Assert.assertEquals(habit.isFirstCheck(), true);
        verify(wholeCakeRepository, times(1));
    }
}
