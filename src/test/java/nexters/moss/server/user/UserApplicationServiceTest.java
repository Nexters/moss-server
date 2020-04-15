package nexters.moss.server.user;

import nexters.moss.server.TestConfiguration;
import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.domain.service.SocialTokenService;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.UserRepository;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.CategoryType;
import nexters.moss.server.domain.value.HabitType;
import nexters.moss.server.domain.service.HabikeryTokenService;
import org.junit.After;
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
    private TestConfiguration testConfiguration;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;

    @Autowired
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;

    @MockBean(name = "socialTokenService")
    private SocialTokenService socialTokenService;

    @Autowired
    private HabikeryTokenService habikeryTokenService;

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

        CategoryType categoryType = setupCategory();
        Habit habit = setupHabit(categoryType, receivingUser);
        SentPieceOfCake sentCake = setupSentPieceOfCake(sendingUser, habit, categoryType);
        ReceivedPieceOfCake receivedCake = setupReceivedPieceOfCake(receivingUser, categoryType, sentCake);

        String reportReason = "기타";

        // when
        Response reportResponse = userApplicationService.report(receivedCake.getId(), reportReason);

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
        return userList.get(userList.size() - 1);
    }

    private CategoryType setupCategory() {
        return CategoryType.builder()
                .habitType(HabitType.WATER)
                .cakeType(CakeType.WATERMELON)
                .build();
    }

    private Habit setupHabit(CategoryType categoryType, User user) {
        Habit habit = Habit.builder()
                .categoryId(categoryType.getId())
                .userId(user.getId())
                .build();
        return habitRepository.save(habit);
    }

    private SentPieceOfCake setupSentPieceOfCake(User sendingUser, Habit habit, CategoryType categoryType) {
        SentPieceOfCake sentCake = SentPieceOfCake.builder()
                .user(sendingUser)
                .categoryId(categoryType.getId())
                .build();
        return pieceOfCakeSendRepository.save(sentCake);
    }

    private ReceivedPieceOfCake setupReceivedPieceOfCake(User receivingUser, CategoryType categoryType, SentPieceOfCake sentCake) {
        ReceivedPieceOfCake receivedCake = ReceivedPieceOfCake.builder()
                .user(receivingUser)
                .sentPieceOfCake(sentCake)
                .categoryId(categoryType.getId())
                .build();
        return pieceOfCakeReceiveRepository.save(receivedCake);
    }
}
