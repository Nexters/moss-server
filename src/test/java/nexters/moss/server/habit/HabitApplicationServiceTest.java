package nexters.moss.server.habit;

import nexters.moss.server.TestConfiguration;
import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.Category;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.*;
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

    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;
    private WholeCakeRepository wholeCakeRepository;

    @Before
    public void setup() {
        testCategory = new Category(1L, HabitType.WATER, CakeType.WATERMELON);

        testUser = userRepository.save(
                new User(
                        null,
                        12345678L,
                        "accountToken",
                        "nickname",
                        null,
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
        pieceOfCakeReceiveRepository = mock(PieceOfCakeReceiveRepository.class);
        wholeCakeRepository = mock(WholeCakeRepository.class);

        given(pieceOfCakeReceiveRepository.countAllByUser_IdAndCategoryId(testUser.getId(), testCategory.getId()))
                .willReturn(8);

        Response<HabitHistory> createResponse = habitApplicationService.createHabit(testUser.getId(), testCategory.getId());
        long habitId = createResponse.getData().getHabitId();

        // when
        habitApplicationService.doneHabit(testUser.getId(), habitId);

        // then
        Habit habit = habitRepository.findById(habitId).get();
        Assert.assertEquals(habit.getIsFirstCheck(), true);
        verify(wholeCakeRepository, times(1));
    }
}
