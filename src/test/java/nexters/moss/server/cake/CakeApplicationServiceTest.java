package nexters.moss.server.cake;

import nexters.moss.server.TestConfiguration;
import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.CreateNewCakeRequest;
import nexters.moss.server.domain.Category;
import nexters.moss.server.domain.habit.Habit;
import nexters.moss.server.domain.cake.SentPieceOfCake;
import nexters.moss.server.domain.user.User;
import nexters.moss.server.domain.habit.HabitRepository;
import nexters.moss.server.domain.cake.SentPieceOfCakeRepository;
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
    private UserRepository userRepository;

    @Autowired
    private SentPieceOfCakeRepository sentPieceOfCakeRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Before
    public void setup() {
        List<HabitType> habitTypes = Arrays.asList(HabitType.values());
        List<CakeType> cakeTypes = Arrays.asList(CakeType.values());

        category = new Category(1L, habitTypes.get(0), cakeTypes.get(0), null, null);
        sender = userRepository.save(new User(null, null, "accounToken", "nickName", null));

        testHabit = habitRepository.save(new Habit(category.getId(), sender.getId()));
    }

    @After
    public void tearDown() {
        testConfiguration.tearDown();
    }

    @Test
    public void createNewCakeTest() {
        CreateNewCakeRequest req = new CreateNewCakeRequest(testHabit.getCategoryId(), "note~!!");
        Response<Long> res = cakeApplicationService.sendCake(sender.getId(), req);

        Assert.assertNotNull(res.getData());

        SentPieceOfCake sentPieceOfCake = sentPieceOfCakeRepository.findById(res.getData()).get();
        Assert.assertEquals(req.getNote(), sentPieceOfCake.getNote());
    }
}

