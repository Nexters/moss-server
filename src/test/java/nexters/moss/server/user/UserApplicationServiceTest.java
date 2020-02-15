package nexters.moss.server.user;

import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.domain.service.SocialTokenService;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.UserRepository;
import nexters.moss.server.domain.service.TokenService;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.HabitType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserApplicationServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;

    @Autowired
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;

    @MockBean(name = "socialTokenService")
    private SocialTokenService socialTokenService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserApplicationService userApplicationService;

    private String testAccessToken;
    private User testUser;

    @Before
    public void setup() {
        testAccessToken = "accessToken";
        testUser = User.builder()
                .socialId(12345678L)
                .nickname("nickname")
                .build();

        given(socialTokenService.getSocialUserId(testAccessToken))
                .willReturn(testUser.getSocialId());
    }

    @Test
    public void joinTest() {
        // given

        // when
        Response joinResponse = userApplicationService.join(testAccessToken, testUser.getNickname());

        // then
        assertThat(joinResponse).isNotNull();
        assertThat(joinResponse.getData()).isNull();

        User resultUser = userRepository.findAll().get(0);
        assertThat(resultUser.getNickname()).isEqualTo(testUser.getNickname());
        assertThat(resultUser.getSocialId()).isEqualTo(testUser.getSocialId());
    }

    @Test
    public void loginTest() {
        // given
        userRepository.save(testUser);

        // when
        Response<String> loginResponse = userApplicationService.login(testAccessToken);

        // then
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getData()).isNotNull();

        User resultUser = userRepository.findAll().get(0);

        assertThat(resultUser.getId()).isEqualTo(testUser.getId());
        assertThat(resultUser.getAccountToken()).isNotNull();
    }

    @Test
    public void leaveTest() {
        // given
        userApplicationService.join(testAccessToken, testUser.getNickname());
        userApplicationService.login(testAccessToken);
        User savedUser = userRepository.findAll().get(0);

        // when
        Response leaveResponse = userApplicationService.leave(savedUser.getAccountToken());

        // then
        assertThat(leaveResponse).isNotNull();
        assertThat(userRepository.count()).isEqualTo(0);
    }

    @Test
    public void getUserInfoTest() {
        // given
        userApplicationService.join(testAccessToken, testUser.getNickname());
        userApplicationService.login(testAccessToken);
        User savedUser = userRepository.findAll().get(0);

        // when
        Response<String> getUserInfoResponse = userApplicationService.getUserInfo(savedUser.getAccountToken());

        // then
        assertThat(getUserInfoResponse).isNotNull();
        assertThat(getUserInfoResponse.getData()).isEqualTo(savedUser.getNickname());
    }

    @Test
    public void reportTest() {
        // given
        User receivingUser = setupJoinAndLogin(testAccessToken, testUser);

        String senderAccessToken = "senderAccessToken";
        User sendingUser = User.builder()
                .socialId(87654321L)
                .nickname("senderNickname")
                .build();
        given(socialTokenService.getSocialUserId(senderAccessToken))
                .willReturn(sendingUser.getSocialId());
        sendingUser = setupJoinAndLogin(senderAccessToken, sendingUser);

        Category category = setupCategory();
        Habit habit = setupHabit(category);
        SentPieceOfCake sentCake = setupSentPieceOfCake(sendingUser, habit, category);
        ReceivedPieceOfCake receivedCake = setupReceivedPieceOfCake(receivingUser, category, sentCake);

        String reportReason = "기타";

        // when
        Response reportResponse = userApplicationService.report(receivingUser.getAccountToken(), receivedCake.getId(), reportReason);

        // then
        assertThat(reportResponse).isNotNull();

        assertThat(reportRepository.count()).isEqualTo(1);
        Report report = reportRepository.findAll().get(0);
        assertThat(report.getUser().getId()).isEqualTo(sendingUser.getId());
        assertThat(report.getReason()).isEqualTo(reportReason);
    }

    private User setupJoinAndLogin(String accessToken, User user) {
        userApplicationService.join(accessToken, user.getNickname());
        userApplicationService.login(accessToken);

        List<User> userList = userRepository.findAll();
        return userList.get(userList.size()-1);
    }

    private Category setupCategory() {
        Category category = Category.builder()
                .habitType(HabitType.WATER)
                .cakeType(CakeType.WATERMELON)
                .build();
        return categoryRepository.save(category);
    }

    private Habit setupHabit(Category category) {
        Habit habit = Habit.builder()
                .category(category)
                .build();
        return habitRepository.save(habit);
    }

    private SentPieceOfCake setupSentPieceOfCake(User sendingUser, Habit habit, Category category) {
        SentPieceOfCake sentCake = SentPieceOfCake.builder()
                .user(sendingUser)
                .category(category)
                .build();
        return pieceOfCakeSendRepository.save(sentCake);
    }

    private ReceivedPieceOfCake setupReceivedPieceOfCake(User receivingUser, Category category, SentPieceOfCake sentCake) {
        ReceivedPieceOfCake receivedCake = ReceivedPieceOfCake.builder()
                .user(receivingUser)
                .sentPieceOfCake(sentCake)
                .category(category)
                .build();
        return pieceOfCakeReceiveRepository.save(receivedCake);
    }
}
