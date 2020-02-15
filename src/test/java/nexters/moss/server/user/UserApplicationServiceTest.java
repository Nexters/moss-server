package nexters.moss.server.user;

import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.service.SocialTokenService;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserApplicationServiceTest {
    @Autowired
    private UserRepository userRepository;

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


        // when


        // then

    }
}
