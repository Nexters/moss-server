package nexters.moss.server.user;

import nexters.moss.server.domain.user.Token;
import nexters.moss.server.domain.user.HabikeryTokenService;
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
    private HabikeryTokenService habikeryTokenService;

    @Test
    public void createTokenTest() {
        // given
        long userId = 12345678L;
        String accessToken = "socialTokenString";

        // when
        String testToken = habikeryTokenService.createToken(userId, accessToken);

        // then
        assertThat(testToken).isNotEmpty();
    }

    @Test
    public void recoverTokenTest() {
        // given
        long userId = 12345678L;
        String accessToken = "socialTokenString";
        String jwtToken = habikeryTokenService.createToken(userId, accessToken);

        // when
        Token testToken = habikeryTokenService.recoverToken(jwtToken);

        // then
        assertThat(testToken.getUserId()).isEqualTo(userId);
        assertThat(testToken.getAccessToken()).isEqualTo(accessToken);
    }
}
