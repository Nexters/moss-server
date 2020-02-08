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
import nexters.moss.server.domain.value.Cake;
import nexters.moss.server.domain.value.HabitStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class HabitApplicationServiceTest {
    private User testUser;

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
        testUser = userRepository.save(
                User.builder()
                        .socialId("socialId")
                        .accountToken("accountToken")
                        .nickname("nickName")
                        .build()
        );

        Habit waterHabit = habitRepository.save(
                Habit.builder()
                        .habitName(nexters.moss.server.domain.value.Habit.WATER)
                        .cakeName(Cake.WATERMELON)
                        .wholeCakeImagePath("WHOLE_CAKE_IMAGE_PATH")
                        .pieceOfCakeImagePath("PIECE_OF_CAKE_IMAGE_PATH")
                        .build()
        );

        Habit stretchingHabit = habitRepository.save(
                Habit.builder()
                        .habitName(nexters.moss.server.domain.value.Habit.STRETCHING)
                        .cakeName(Cake.WALNUT)
                        .wholeCakeImagePath("WHOLE_CAKE_IMAGE_PATH")
                        .pieceOfCakeImagePath("PIECE_OF_CAKE_IMAGE_PATH")
                        .build()
        );

        LocalDateTime date = LocalDateTime.now();
        for (int i = 0; i < 5; i++) {
            habitRecordRepository.save(
                    HabitRecord.builder()
                            .user(testUser)
                            .habit(waterHabit)
                            .habitStatus(HabitStatus.NOT_DONE)
                            .date(date)
                            .build()
            );

            habitRecordRepository.save(
                    HabitRecord.builder()
                            .user(testUser)
                            .habit(stretchingHabit)
                            .habitStatus(HabitStatus.NOT_DONE)
                            .date(date)
                            .build()
            );
        }
    }

    @Test
    public void getHabitHistoryTest() {
        Response<List<HabitHistory>> response = habitApplicationService.getHabitHistory(testUser.getId());
        Assert.assertEquals(2, response.getData().size());

        for(HabitHistory habitHistory : response.getData()) {
            Assert.assertEquals(5, habitHistory.getHabitRecords().size());
        }

        return;
    }
}
