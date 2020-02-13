package nexters.moss.server.cake;

import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.dto.cake.CreateANewCakeRequest;
import nexters.moss.server.application.dto.cake.GetANewCakeResponse;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.Habit;
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
        habitRepository.save(new Habit(null, "물마시기", "치즈", null, null));
        habitRepository.save(new Habit(null, "명", "오레오", null, null));
        habitRepository.save(new Habit(null, "뉴스보기", "딸기", null, null));
        habitRepository.save(new Habit(null, "일기쓰", "키캣", null, null));
        habitRepository.save(new Habit(null, "스트레림", "카라멜", null, null));
        testHabit = habitRepository.save(new Habit(null, "책읽", "생크", null, null));

        sender = userRepository.save(new User(null, "socialId", "accounToken", "nickName", null, null, null, null, null));
        receiver = userRepository.save(new User(null, "socialId2", "accounToken2", "nickName2", null, null, null, null, null));

        pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, testHabit, "hello" , null));
        pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, testHabit, "hello123" , null));
        pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, testHabit, "hello123456" , null));
    }

    @Test
    public void CreateANewCakeTest(){
        CreateANewCakeRequest req = new CreateANewCakeRequest(sender.getId(), testHabit.getId(), "note~!!");
        Assert.assertNotNull(cakeApplicationService.CreateANewCake(req).getData());
    }

    @Test
    public void GetANewCakeTest(){
        long userId = receiver.getId();
        long habitId = testHabit.getId();

        Response<GetANewCakeResponse> testResponse = cakeApplicationService.GetANewCake(userId, habitId);
        Assert.assertNotNull(testResponse.getData());
    }
}

