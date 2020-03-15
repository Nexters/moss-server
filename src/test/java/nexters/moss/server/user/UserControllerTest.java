package nexters.moss.server.user;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.UserRepository;
import nexters.moss.server.domain.service.SocialTokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private SocialTokenService socialTokenService;

    private String userAccessToken;
    private long userSocialId;
    private String userNickname;
    private String userHabikeryToken;

    @Before
    public void setup() {
        userAccessToken = "testAccessToken";
        userSocialId = 12341234L;
        userNickname = "testNickname";

        when(socialTokenService.getSocialUserId(userAccessToken)).thenReturn(userSocialId);
    }

    @Test
    public void userJoinTest_shouldSuccess() throws URISyntaxException {
        // given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("accessToken", userAccessToken);

        // when
        Response response = testRestTemplate.postForObject(
                new URI("/user"),
                new HttpEntity<>(userNickname, httpHeaders),
                Response.class
        );

        // then
        assertThat(response).isNotNull();

        User resultUser = userRepository.findBySocialId(userSocialId).get();
        assertThat(resultUser).isNotNull();
        assertThat(resultUser.getNickname()).isEqualTo(userNickname);
    }
    @Test
    public void userJoinTest_shouldFail_withWrongSocialId() {

    }
    @Test
    public void userJoinTest_shouldFail_withExistingUser() {

    }

    @Test
    public void userLeaveTest_shouldSuccess() {

    }
    @Test
    public void userLeaveTest_shouldFail_withUnauthorizedToken() {

    }
    @Test
    public void userLeaveTest_shouldFail_withNotFoundUser() {

    }

    @Test
    public void getUserInfoTest_shouldSuccess() {

    }
    @Test
    public void getUserInfoTest_shouldFail_withUnauthorizedToken() {

    }
    @Test
    public void getUserInfoTest_shouldFail_withNotFoundUser() {

    }

    @Test
    public void userReportTest_shouldSuccess() {

    }
    @Test
    public void userReportTest_shouldFail_withUnauthorizedToken() {

    }
    @Test
    public void userReportTest_shouldFail_withNotFoundCakeId() {

    }
    @Test
    public void userReportTest_shouldFail_withNotFoundReportedUserId() {

    }
}
