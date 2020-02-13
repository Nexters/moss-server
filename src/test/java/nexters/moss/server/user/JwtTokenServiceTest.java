package nexters.moss.server.user;

import nexters.moss.server.domain.model.Token;
import nexters.moss.server.domain.service.TokenService;
import nexters.moss.server.infra.auth.JwtTokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTokenServiceTest {
    @Autowired
    private TokenService tokenService;

    @Test
    public void createTokenTest() {
        // given
        long userId = 12345678L;
        String accessToken = "socialTokenString";
        String accountToken = "accountTokenString";

        // when
        String testToken = tokenService.createToken(userId, accessToken, accountToken);

        // then
        assertThat(testToken).isNotEmpty();
    }

    @Test
    public void recoverTokenTest() {
        // given
        long userId = 12345678L;
        String accessToken = "socialTokenString";
        String accountToken = "accountTokenString";
        String jwtToken = tokenService.createToken(userId, accessToken, accountToken);

        // when
        Token testToken = tokenService.recoverToken(jwtToken);

        // then
        assertThat(testToken.getUserId()).isEqualTo(userId);
        assertThat(testToken.getAccessToken()).isEqualTo(accessToken);
        assertThat(testToken.getAccountToken()).isEqualTo(accountToken);
    }
}
