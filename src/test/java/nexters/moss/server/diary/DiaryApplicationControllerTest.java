package nexters.moss.server.diary;

import nexters.moss.server.TestConfiguration;
import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.diary.DiaryDTO;
import nexters.moss.server.application.dto.diary.HistoryResponse;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.domain.service.SocialTokenService;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.HabitType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
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

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestConfiguration testConfiguration;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserApplicationService userApplicationService;
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

        category = categoryRepository.save(new Category(null, habitTypes.get(0), cakeTypes.get(0)));
        descriptionRepository.save(new Description(null, category, "receivePieceOfCake", "diary"));

        habit = habitRepository.save(new Habit(null, category, receiver.getId(), null, 0, false, false, 0));

        sentPieceOfCake = pieceOfCakeSendRepository.save(new SentPieceOfCake(null, sender, category, "note", null));
        pieceOfCakeReceiveRepository.save(new ReceivedPieceOfCake(null, receiver, sentPieceOfCake, category));
        wholeCake = wholeCakeRepository.save(new WholeCake(null, receiver, habit, category));
    }

    @After
    public void tearDown() {
        testConfiguration.tearDown();
    }

    @Test
    public void getPieceOfCakeDiaryTest() throws URISyntaxException {

        ResponseEntity<Response<Object>> res = getTestResponse("/api/diary/piece", new ParameterizedTypeReference<Response<List<DiaryDTO>>>(){});
        List<DiaryDTO> diaries = (List<DiaryDTO>) res.getBody().getData();

        assertThat(res.getBody().getData()).isNotNull();
        assertThat(diaries.get(0).getCakeName())
                                .isEqualTo(sentPieceOfCake.getCategory()
                                                          .getCakeType()
                                                          .getName());
            }

    @Test
    public void getWholeCakeDiaryTest() throws URISyntaxException {

        ResponseEntity<Response<Object>> res = getTestResponse("/api/diary/whole", new ParameterizedTypeReference<Response<List<DiaryDTO>>>(){});

        List<DiaryDTO> diaries = (List<DiaryDTO>) res.getBody().getData();

        assertThat(res.getBody().getData()).isNotNull();
        assertThat(diaries.get(0).getCakeName())
                        .isEqualTo(wholeCake.getCategory()
                                            .getCakeType()
                                            .getName());
    }

    @Test
    public void getCakeHistoryTest() throws URISyntaxException {

        ResponseEntity<Response<Object>> res = getTestResponse("/api/diary/history?categoryId=" + category.getId(), new ParameterizedTypeReference<Response<HistoryResponse>>(){});

        HistoryResponse historyResponse = (HistoryResponse) res.getBody().getData();

        assertThat(historyResponse).isNotNull();
        assertThat(historyResponse.getCakeName())
                                  .isEqualTo(wholeCake.getCategory()
                                                      .getCakeType()
                                                      .getName());

    }

    private ResponseEntity<Response<Object>> getTestResponse(String uri, ParameterizedTypeReference parameterizedTypeReference) throws URISyntaxException {
        URI req_uri = new URI(uri);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("habikeryToken", receiverHabikeryToken);
        HttpEntity<Map<String,Object>> req = new HttpEntity<>(httpHeaders);

        return this.testRestTemplate.exchange(uri
                ,HttpMethod.GET
                ,req
                ,parameterizedTypeReference);
    }
}
