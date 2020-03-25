package nexters.moss.server.cake;

import nexters.moss.server.TestConfiguration;
import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.CreateNewCakeRequest;
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
import org.junit.After;
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
    private Category category;

    @Autowired
    private TestConfiguration testConfiguration;

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

        for (int i = 1; i < habitTypes.size(); i++) {
            categoryRepository.save(new Category(null, habitTypes.get(i), cakeTypes.get(i)));
        }
        category = categoryRepository.save(new Category(null, habitTypes.get(0), cakeTypes.get(0)));
        sender = userRepository.save(new User(null, null, "accounToken", "nickName", null, null, null));

        testHabit = habitRepository.save(new Habit(null, category, sender, null, 0, false, false));
    }

    @After
    public void tearDown() {
        testConfiguration.tearDown();
    }

    @Test
    public void createNewCakeTest() {
        CreateNewCakeRequest req = new CreateNewCakeRequest(testHabit.getCategory().getId(), "note~!!");
        Response<Long> res = cakeApplicationService.sendCake(sender.getId(), req);

        Assert.assertNotNull(res.getData());

        SentPieceOfCake sentPieceOfCake = pieceOfCakeSendRepository.findById(res.getData()).get();
        Assert.assertEquals(req.getNote(), sentPieceOfCake.getNote());

    }
}

