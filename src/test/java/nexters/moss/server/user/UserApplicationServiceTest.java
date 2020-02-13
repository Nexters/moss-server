package nexters.moss.server.user;

import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.service.SocialTokenService;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class UserApplicationServiceTest {
    @Autowired
    private UserRepository userRepository;

    @MockBean(name = "socialTokenService")
    private SocialTokenService socialTokenService;

    @Autowired
    private UserApplicationService userApplicationService;

    @Test
    public void joinTest() {
        // given
        String accessToken = "accessToken";
        String nickname = "nickname";
        Long socialId = 12345678L;

        given(socialTokenService.getSocialUserId(accessToken))
                .willReturn(socialId);

        // when
        Response<Long> joinResponse = userApplicationService.join(accessToken, nickname);

        // then
        assertThat(joinResponse).isNotNull();

        User user = userRepository.findAll().get(0);

        assertThat(user.getId()).isEqualTo(joinResponse.getData().longValue());
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getSocialId()).isEqualTo(socialId);
    }
}
