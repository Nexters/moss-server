package nexters.moss.server.habit;

import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.Category;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.domain.service.HabitRecordService;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class HabitApplicationServiceTest {
    private User testUser;

    private Habit testHabit;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private HabitApplicationService habitApplicationService;

    @Autowired
    private HabitRecordService habitRecordService;

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
        Category category = new Category(null, HabitType.WATER, CakeType.WATERMELON);
        category = categoryRepository.save(category);

        testUser = userRepository.save(
                new User(
                        null,
                        12345678L,
                        "accountToken",
                        "nickname",
                        null
                )
        );
        testHabit = habitRepository.save(
                new Habit(
                        null,
                        category,
                        testUser,
                        null,
                        0,
                        false,
                        false
                )
        );

        List<HabitRecord> habitRecords = habitRecordService.createHabitRecords(testUser, testHabit);
        habitRecordRepository.saveAll(habitRecords);

        testHabit = habitRepository.save(
                new Habit(
                        testHabit.getId(),
                        testHabit.getCategory(),
                        testUser,
                        habitRecords,
                        testHabit.getOrder(),
                        false,
                        false
                )
        );
    }

    @Test
    // TODO
    public void getHabitHistoryTest() {
//        Response<HabitHistory> createResponse = habitApplicationService.createHabit(testUser.getId(), testHabit.getId());
//        Response<List<HabitHistory>> getResponse = habitApplicationService.getHabit(testUser.getId());
//        Assert.assertEquals(1, getResponse.getData().size());
//
//        for (HabitHistory habitHistory : getResponse.getData()) {
//            Assert.assertEquals(5, habitHistory.getHabitRecords().size());
//        }
    }

    @Test
    public void createHabitTest() {
        Response<HabitHistory> createResponse = habitApplicationService.createHabit(testUser.getId(), testHabit.getId());
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
        habitApplicationService.createHabit(testUser.getId(), testHabit.getId());
        habitApplicationService.deleteHabit(testUser.getId(), testHabit.getId());
        Assert.assertEquals(0, habitRecordRepository.findAllByUser_IdAndHabit_Id(testUser.getId(), testHabit.getId()).size());
    }

//    @Test
//    public void doneHabitTest() {
//        // given
//        pieceOfCakeReceiveRepository = mock(PieceOfCakeReceiveRepository.class);
//        wholeCakeRepository = mock(WholeCakeRepository.class);
//
//        given(pieceOfCakeReceiveRepository.countAllByUser_IdAndCategory_Id(testUser.getId(), testHabit.getCategory().getId()))
//                .willReturn(8);
//
//        habitApplicationService.createHabit(testUser.getId(), testHabit.getId());
//
//        // when
//        habitApplicationService.doneHabit(testHabit.getId(), testHabit.getId());
//
//        // then
//        Habit habit = habitRepository.findById(testHabit.getId()).get();
//        Assert.assertEquals(habit.getIsFirstCheck(), true);
//        verify(wholeCakeRepository, times(1));
//    }
}
