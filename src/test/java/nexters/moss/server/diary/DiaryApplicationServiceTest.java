package nexters.moss.server.diary;

import nexters.moss.server.application.DiaryApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.diary.DiaryDTO;
import nexters.moss.server.application.dto.diary.HistoryResponse;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
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
    private DiaryApplicationService diaryApplicationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private HabitRecordRepository habitRecordRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DescriptionRepository descriptionRepository;
    @Autowired
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;
    @Autowired
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;
    @Autowired
    private WholeCakeRepository wholeCakeRepository;

    @Before
    public void setup() {
        sender = userRepository.save(new User(null, null, "accounToken", "sender", null));
        receiver = userRepository.save(new User(null, null, "accounToken", "reciever", null));
        category = categoryRepository.save(new Category(null, HabitType.BREAKFAST, CakeType.APPLE));

        habit = habitRepository.save(new Habit(null, category, receiver, null, false, false));
        habitRecordRepository.save(new HabitRecord(null,receiver, habit, null, null));
        descriptionRepository.save(new Description(null, category, "receivePieceOfCake", "diary", "cakeHistory"));

        sentPieceOfCake = pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, category, "note~!!!", null, null));
        pieceOfCakeReceiveRepository.save(new ReceivedPieceOfCake(null, receiver, sentPieceOfCake, category, null ));
        wholeCake = wholeCakeRepository.save(new WholeCake(null, receiver, habit, category, null));

    }

    @Test
    public void getPieceOfCakeDiaryTest() {
        Long userId = receiver.getId();

        Response<List<DiaryDTO>> res =  diaryApplicationService.getPieceOfCakeDiary(userId);
        Assert.assertNotNull(res.getData());
        Assert.assertEquals(sentPieceOfCake.getCategory().getCakeType().getName(), res.getData().get(0).getCakeName());
    }

    @Test
    public void getWholeCakeDiaryTest() {
        Long userId = receiver.getId();

        Response<List<DiaryDTO>> res =  diaryApplicationService.getWholeCakeDiary(userId);
        Assert.assertNotNull(res.getData());
        Assert.assertEquals(wholeCake.getCategory().getCakeType().getName(), res.getData().get(0).getCakeName());
    }

    @Test
    public void getCakeHistoryTest() {
        Long userId = receiver.getId();
        Long categoryId = category.getId();

        Response<HistoryResponse> res = diaryApplicationService.getCakeHistory(userId, categoryId);
        Assert.assertNotNull(res.getData());
        Assert.assertEquals(wholeCake.getCategory().getCakeType().getName(), res.getData().getCakeName());
    }
}
