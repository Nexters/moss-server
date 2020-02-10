package nexters.moss.server.cake;

import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.ReceivedPieceOfCake;
import nexters.moss.server.domain.model.SentPieceOfCake;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.HabitRepository;
import nexters.moss.server.domain.repository.PieceOfCakeSendRepository;
import nexters.moss.server.domain.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CakeApplicationServiceTest {
    private Habit testHabit;
    private User sender;
    private User receiver;

    @Autowired
    private CakeApplicationService cakeApplicationService;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;

    @Before
    public void setup() {
        habitRepository.save(new Habit(null, "123", "월", null, null));
        habitRepository.save(new Habit(null, "1234", "화", null, null));
        habitRepository.save(new Habit(null, "12345", "수", null, null));
        habitRepository.save(new Habit(null, "123456", "목", null, null));
        habitRepository.save(new Habit(null, "1234567", "금", null, null));
        testHabit = habitRepository.save(new Habit(null, "물마시기", "토", null, null));

        sender = userRepository.save(new User(null, "socialId", "accounToken", "nickName", null, null, null, null, null));
        receiver = userRepository.save(new User(null, "socialId2", "accounToken2", "nickName2", null, null, null, null, null));

        pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, testHabit, "hello" , null));
        pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, testHabit, "hello123" , null));
        pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, testHabit, "hello123456" , null));
    }

    @Test
    public void getCategoriesTest(){
        Response<List<Habit>> testResponse = cakeApplicationService.getAllHabits();

        Assert.assertNotNull(testResponse.getData());
        Assert.assertEquals(6, testResponse.getData().size());

        return;
    }


    @Test
    public void addSentPieceOfCakeTest(){
        long userId = sender.getId();
        long categoryId = testHabit.getId();
        Map<String, Object> testData =  new HashMap<>();
        testData.put("categoryId", categoryId);
        testData.put("message", "123");

        Response<SentPieceOfCake> testResponse = cakeApplicationService.addSentPieceOfCake(userId, testData);
        Assert.assertNotNull(testResponse.getData());
        Assert.assertEquals("123", testResponse.getData().getNote());

        return;
    }

    @Test
    public void addReceivedPieceOfCake(){
        long userId = receiver.getId();
        long categoryId = testHabit.getId();

        Response<ReceivedPieceOfCake> testResponse = cakeApplicationService.addReceivedPieceOfCake(userId, categoryId);
        Assert.assertNotNull(testResponse.getData());

        return;
    }
}

