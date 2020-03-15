package nexters.moss.server.user;

import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
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
public class TokenControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private SocialTokenService socialTokenService;
    @Autowired
    private UserApplicationService userApplicationService;

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
    public void userLoginTest_shouldSuccess() throws URISyntaxException {
        // given
        userApplicationService.join(userAccessToken, userNickname);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("accessToken", userAccessToken);

        // when
        Response<String> response = testRestTemplate.postForObject(
                new URI("/token"),
                new HttpEntity<>(null, httpHeaders),
                Response.class
        );

        // then
        assertThat(response.getData()).isNotBlank();
    }
    @Test
    public void userLoginTest_shouldFail_withWrongSocialId() {

    }
}
