package nexters.moss.server.cake;

import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.CreateANewCakeRequest;
import nexters.moss.server.application.dto.cake.NewCakeDTO;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.SentPieceOfCake;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.HabitRepository;
import nexters.moss.server.domain.repository.PieceOfCakeSendRepository;
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

import java.util.Arrays;
import java.util.List;

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
        List<HabitType> habitTypes = Arrays.asList(HabitType.values());
        List<CakeType> cakeTypes = Arrays.asList(CakeType.values());

        for (int i=1; i<habitTypes.size(); i++){
            habitRepository.save(new Habit(null, habitTypes.get(i), cakeTypes.get(i),null, null));
        }

        testHabit = habitRepository.save(new Habit(null, habitTypes.get(0), cakeTypes.get(0), null, null));


        sender = userRepository.save(new User(null, null, "accounToken", "nickName", null, null, null, null));
        receiver = userRepository.save(new User(null, null, "accounToken2", "nickName2", null, null, null, null));

        pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, testHabit, "hello" , null));
        pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, testHabit, "hello123" , null));
        pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, testHabit, "hello123456" , null));
    }

    @Test
    public void createANewCakeTest(){
        CreateANewCakeRequest req = new CreateANewCakeRequest(sender.getId(), testHabit.getId(), "note~!!");
        Response<Long> res = cakeApplicationService.createANewCake(req);
        Assert.assertNotNull(res.getData());

        SentPieceOfCake sentPieceOfCake = pieceOfCakeSendRepository.findById(res.getData()).get();
        Assert.assertEquals(req.getNote(), sentPieceOfCake.getNote());

    }

    @Test
    public void getANewCakeTest(){
        long userId = receiver.getId();
        long habitId = testHabit.getId();

        Response<NewCakeDTO> testResponse = cakeApplicationService.getANewCake(userId, habitId);
        Assert.assertNotNull(testResponse.getData());
    }
}

