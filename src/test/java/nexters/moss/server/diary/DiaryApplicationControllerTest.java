package nexters.moss.server.diary;

import nexters.moss.server.TestConfiguration;
import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.diary.DiaryDTO;
import nexters.moss.server.application.dto.diary.HistoryResponse;
import nexters.moss.server.domain.Category;
import nexters.moss.server.domain.cake.*;
import nexters.moss.server.domain.habit.Habit;
import nexters.moss.server.domain.habit.HabitRepository;
import nexters.moss.server.domain.user.SocialTokenService;
import nexters.moss.server.domain.user.User;
import nexters.moss.server.domain.user.UserRepository;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.Description;
import nexters.moss.server.domain.value.HabitType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DiaryApplicationControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    private TestConfiguration testConfiguration;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserApplicationService userApplicationService;
    @Autowired
    private SentPieceOfCakeRepository sentPieceOfCakeRepository;
    @Autowired
    private ReceivedPieceOfCakeRepository receivedPieceOfCakeRepository;
    @Autowired
    private WholeCakeRepository wholeCakeRepository;
    @Autowired
    private HabitRepository habitRepository;


    @MockBean(name = "socialTokenService")
    private SocialTokenService socialTokenService;

    private String receiverAccessToken;

    private String receiverHabikeryToken;

    private User receiver;
    private User sender;

    private Habit habit;
    private Category category;
    private SentPieceOfCake sentPieceOfCake;
    private WholeCake wholeCake;

    @Before
    public void setup() {
        receiverAccessToken = "receiverAccessToken";

        sender = User.builder()
                .socialId(12345678L)
                .nickname("sender")
                .build();

        receiver = User.builder()
                .socialId(87654321L)
                .nickname("receiver")
                .build();

        given(socialTokenService.getSocialUserId(receiverAccessToken))
                .willReturn(receiver.getSocialId());

        userRepository.save(sender);
        userRepository.save(receiver);

        receiverHabikeryToken = userApplicationService.login(receiverAccessToken).getData();

        List<HabitType> habitTypes = Arrays.asList(HabitType.values());
        List<CakeType> cakeTypes = Arrays.asList(CakeType.values());

        category = new Category(1L, habitTypes.get(0), cakeTypes.get(0), new Description("receivePieceOfCake"), new Description("diary"));

        habit = habitRepository.save(new Habit(null, category.getId(), receiver.getId(), null, 0, false, 0));

        sentPieceOfCake = sentPieceOfCakeRepository.save(new SentPieceOfCake(null, sender.getId(), category.getId(), "note", null));
        receivedPieceOfCakeRepository.save(new ReceivedPieceOfCake(null, receiver.getId(), sentPieceOfCake.getId(), category.getId()));
        wholeCake = wholeCakeRepository.save(new WholeCake(null, receiver.getId(), category.getId()));
    }

    @After
    public void tearDown() {
        testConfiguration.tearDown();
    }

    @Test
    public void getPieceOfCakeDiaryTest() {
        ResponseEntity<Response<Object>> res = getTestResponse("/api/diary/piece", new ParameterizedTypeReference<Response<List<DiaryDTO>>>() {
        });
        List<DiaryDTO> diaries = (List<DiaryDTO>) res.getBody().getData();

        assertThat(res.getBody().getData()).isNotNull();
        assertThat(diaries.get(0).getCakeName())
                .isEqualTo(
                        category
                                .getCakeType()
                                .getName()
                );
    }

    @Test
    public void getWholeCakeDiaryTest() {
        ResponseEntity<Response<Object>> res = getTestResponse("/api/diary/whole", new ParameterizedTypeReference<Response<List<DiaryDTO>>>() {
        });

        List<DiaryDTO> diaries = (List<DiaryDTO>) res.getBody().getData();

        assertThat(res.getBody().getData()).isNotNull();
        assertThat(diaries.get(0).getCakeName())
                .isEqualTo(
                        category
                                .getCakeType()
                                .getName()
                );
    }

    @Test
    public void getCakeHistoryTest() {
        ResponseEntity<Response<Object>> res = getTestResponse("/api/diary/history?categoryId=" + category.getId(), new ParameterizedTypeReference<Response<HistoryResponse>>() {
        });

        HistoryResponse historyResponse = (HistoryResponse) res.getBody().getData();

        assertThat(historyResponse).isNotNull();
        assertThat(historyResponse.getCakeName())
                .isEqualTo(
                        category
                                .getCakeType()
                                .getName()
                );
    }

    private ResponseEntity<Response<Object>> getTestResponse(String uri, ParameterizedTypeReference parameterizedTypeReference) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("habikeryToken", receiverHabikeryToken);
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(httpHeaders);

        return this.testRestTemplate.exchange(uri
                , HttpMethod.GET
                , req
                , parameterizedTypeReference);
    }
}
