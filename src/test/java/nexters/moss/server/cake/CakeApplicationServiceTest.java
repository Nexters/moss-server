package nexters.moss.server.cake;

import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.CreateNewCakeRequest;
import nexters.moss.server.application.dto.cake.NewCakeDTO;
import nexters.moss.server.domain.model.Category;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.SentPieceOfCake;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.CategoryRepository;
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
    private Category category;

    @Autowired
    private CakeApplicationService cakeApplicationService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Before
    public void setup() {
        List<HabitType> habitTypes = Arrays.asList(HabitType.values());
        List<CakeType> cakeTypes = Arrays.asList(CakeType.values());

        for (int i=1; i<habitTypes.size(); i++){
            categoryRepository.save(new Category(null, habitTypes.get(i), cakeTypes.get(i)));
        }
        category = categoryRepository.save(new Category(null, habitTypes.get(0), cakeTypes.get(0)));
        testHabit = habitRepository.save(new Habit(null, category, null, null, false, false));

        sender = userRepository.save(new User(null, null, "accounToken", "nickName", null));
        receiver = userRepository.save(new User(null, null, "accounToken2", "nickName2", null));

        pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, category,"hello" , null, null));
        pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, category, "hello123" , null, null));
        pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, category, "hello123456" , null, null));
    }

    @Test
    public void createANewCakeTest(){
        CreateNewCakeRequest req = new CreateNewCakeRequest(sender.getId(), testHabit.getId(), "note~!!");
        Response<Long> res = cakeApplicationService.createNewCake(req);
        Assert.assertNotNull(res.getData());

        SentPieceOfCake sentPieceOfCake = pieceOfCakeSendRepository.findById(res.getData()).get();
        Assert.assertEquals(req.getNote(), sentPieceOfCake.getNote());

    }

    @Test
    public void getANewCakeTest(){
        long userId = receiver.getId();
        long habitId = testHabit.getId();

        Response<NewCakeDTO> testResponse = cakeApplicationService.getNewCake(userId, habitId);
        Assert.assertNotNull(testResponse.getData());
    }
}

