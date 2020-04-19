package nexters.moss.server.user;

import nexters.moss.server.TestConfiguration;
import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.Category;
import nexters.moss.server.domain.cake.ReceivedPieceOfCake;
import nexters.moss.server.domain.cake.ReceivedPieceOfCakeRepository;
import nexters.moss.server.domain.cake.SentPieceOfCake;
import nexters.moss.server.domain.cake.SentPieceOfCakeRepository;
import nexters.moss.server.domain.habit.Habit;
import nexters.moss.server.domain.habit.HabitRepository;
import nexters.moss.server.domain.user.*;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.HabitType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class UserApplicationServiceTest {
    @Autowired
    private TestConfiguration testConfiguration;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private SentPieceOfCakeRepository sentPieceOfCakeRepository;

    @Autowired
    private ReceivedPieceOfCakeRepository receivedPieceOfCakeRepository;

    @MockBean(name = "socialTokenService")
    private SocialTokenService socialTokenService;

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

    @After
    public void tearDown() {
        testConfiguration.tearDown();
    }

    @Test
    public void joinTest() {
        // given

        // when
        Response joinResponse = userApplicationService.join(testAccessToken, testUser.getNickname());

        // then
        assertThat(joinResponse).isNotNull();
        assertThat(joinResponse.getData()).isNull();

        List<User> userList = userRepository.findAll();
        User resultUser = userList.get(userList.size() - 1);
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

        List<User> userList = userRepository.findAll();
        User resultUser = userList.get(userList.size() - 1);

        assertThat(resultUser.getId()).isEqualTo(testUser.getId());
        assertThat(resultUser.getHabikeryToken()).isNotNull();
    }

    @Test
    public void getUserInfoTest() {
        // given
        userApplicationService.join(testAccessToken, testUser.getNickname());
        userApplicationService.login(testAccessToken);
        List<User> userList = userRepository.findAll();
        User savedUser = userList.get(userList.size() - 1);

        // when
        Response<String> getUserInfoResponse = userApplicationService.getUserInfo(savedUser.getId());

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
        Habit habit = setupHabit(category, receivingUser);
        SentPieceOfCake sentCake = setupSentPieceOfCake(sendingUser, habit, category);
        ReceivedPieceOfCake receivedCake = setupReceivedPieceOfCake(receivingUser, category, sentCake);

        String reportReason = "기타";

        // when
        Response reportResponse = userApplicationService.report(receivedCake.getId(), reportReason);

        // then
        assertThat(reportResponse).isNotNull();

        assertThat(reportRepository.count()).isEqualTo(1);
        Report report = reportRepository.findAll().get(0);
        assertThat(report.getReason()).isEqualTo(reportReason);
    }

    @Test
    public void leaveTest() {
        // given
        userApplicationService.join(testAccessToken, testUser.getNickname());
        userApplicationService.login(testAccessToken);
        List<User> userList = userRepository.findAll();
        int userCount = userList.size();
        User savedUser = userList.get(userCount - 1);

        // when
        Response leaveResponse = userApplicationService.leave(savedUser.getId());

        // then
        assertThat(leaveResponse).isNotNull();
        assertThat(userRepository.count()).isEqualTo(userCount - 1);
    }

    private User setupJoinAndLogin(String accessToken, User user) {
        userApplicationService.join(accessToken, user.getNickname());
        userApplicationService.login(accessToken);

        List<User> userList = userRepository.findAll();
        return userList.get(userList.size() - 1);
    }

    private Category setupCategory() {
        return Category.builder()
                .habitType(HabitType.WATER)
                .cakeType(CakeType.WATERMELON)
                .build();
    }

    private Habit setupHabit(Category category, User user) {
        Habit habit = Habit.builder()
                .categoryId(category.getId())
                .userId(user.getId())
                .build();
        return habitRepository.save(habit);
    }

    private SentPieceOfCake setupSentPieceOfCake(User sendingUser, Habit habit, Category category) {
        SentPieceOfCake sentCake = SentPieceOfCake.builder()
                .userId(sendingUser.getId())
                .categoryId(category.getId())
                .build();
        return sentPieceOfCakeRepository.save(sentCake);
    }

    private ReceivedPieceOfCake setupReceivedPieceOfCake(User receivingUser, Category category, SentPieceOfCake sentCake) {
        ReceivedPieceOfCake receivedCake = ReceivedPieceOfCake.builder()
                .userId(receivingUser.getId())
                .sentPieceOfCakeId(sentCake.getId())
                .categoryId(category.getId())
                .build();
        return receivedPieceOfCakeRepository.save(receivedCake);
    }
}
