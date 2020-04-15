package nexters.moss.server.diary;

import nexters.moss.server.TestConfiguration;
import nexters.moss.server.application.DiaryApplicationService;
import nexters.moss.server.application.dto.Image;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.diary.DiaryDTO;
import nexters.moss.server.application.dto.diary.HistoryResponse;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.Description;
import nexters.moss.server.domain.value.HabitType;
import nexters.moss.server.application.value.ImageEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DiaryApplicationServiceTest {
    private User receiver;
    private User sender;

    private Habit habit;
    private Category category;
    private SentPieceOfCake sentPieceOfCake;
    private WholeCake wholeCake;

    @Autowired
    private TestConfiguration testConfiguration;
    @Autowired
    private Image image;
    @Autowired
    private DiaryApplicationService diaryApplicationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;
    @Autowired
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;
    @Autowired
    private WholeCakeRepository wholeCakeRepository;

    @Before
    public void setup() {
        sender = userRepository.save(new User(null, null, "accountToken", "sender", null, null));
        receiver = userRepository.save(new User(null, null, "accountToken", "receiver", null, null));
        category = new Category(1L, HabitType.WATER, CakeType.WATERMELON,  new Description("receivePieceOfCake"), new Description("diary"));

        habit = habitRepository.save(new Habit(null, category.getId(), receiver.getId(), null, 0, false, false, 0));

        sentPieceOfCake = pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, category.getId(), "note", null));
        pieceOfCakeReceiveRepository.save(new ReceivedPieceOfCake(null, receiver, sentPieceOfCake, category.getId()));
        wholeCake = wholeCakeRepository.save(new WholeCake(null, receiver, habit.getId(), category.getId()));

    }

    @After
    public void tearDown() {
        testConfiguration.tearDown();
    }

    @Test
    public void getPieceOfCakeDiaryTest() {
        Long userId = receiver.getId();

        Response<List<DiaryDTO>> res = diaryApplicationService.getPieceOfCakeDiary(userId);
        Assert.assertNotNull(res.getData());
        Assert.assertEquals(category.getCakeType().getName(), res.getData().get(0).getCakeName());

        String imagePath = image.getUrl() + "/" + category.getHabitType().getKey() + "/" + ImageEvent.PIECE_OF_CAKE_DIARY.getName() + "_" + res.getData().get(0).getCount() + ".png";
        Assert.assertEquals(imagePath, res.getData().get(0).getImagePath());

    }

    @Test
    public void getWholeCakeDiaryTest() {
        Long userId = receiver.getId();

        Response<List<DiaryDTO>> res = diaryApplicationService.getWholeCakeDiary(userId);
        Assert.assertNotNull(res.getData());
        Assert.assertEquals(category.getCakeType().getName(), res.getData().get(0).getCakeName());

        String imagePath = image.getUrl() + "/" + category.getHabitType().getKey() + "/" + ImageEvent.WHOLE_CAKE_DIARY.getName() + ".png";
        Assert.assertEquals(imagePath, res.getData().get(0).getImagePath());

    }

    @Test
    public void getCakeHistoryTest() {
        Long userId = receiver.getId();
        Long categoryId = category.getId();

        Response<HistoryResponse> res = diaryApplicationService.getCakeHistory(userId, categoryId);
        Assert.assertNotNull(res.getData());
        Assert.assertEquals(category.getCakeType().getName(), res.getData().getCakeName());

        String imagePath = image.getUrl() + "/" + category.getHabitType().getKey() + "/" + ImageEvent.HISTORY.getName() + ".gif";
        Assert.assertEquals(imagePath, res.getData().getImagePath());

    }
}
