package nexters.moss.server.application;

import nexters.moss.server.domain.model.SocialTokenService;
import nexters.moss.server.domain.model.TokenService;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.UserRepository;
import nexters.moss.server.infra.auth.JwtTokenService;
import nexters.moss.server.infra.auth.KakaoTokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

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

        given(socialTokenService.querySocialUserId(accessToken))
                .willReturn(socialId);

        // when
        userApplicationService.join(accessToken, nickname);

        // then
        User user = userRepository.findAll().get(0);

        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getSocialId()).isEqualTo(socialId);
    }
}
