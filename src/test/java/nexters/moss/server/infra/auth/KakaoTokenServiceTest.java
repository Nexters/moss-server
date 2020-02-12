package nexters.moss.server.infra.auth;

import nexters.moss.server.domain.model.SocialTokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
//public class KakaoTokenServiceTest {
//    private SocialTokenService socialTokenService;
//
//    @Before
//    public void setup() {
//        this.socialTokenService = new KakaoTokenService();
//    }
//
//    @Test
//    public void querySocialUserIdTest() {
//        // given
//        Long validatedUserId = 1279527596L;
//        String accessToken = "V4rk6DVJrpvH31wM-bXjVf0Uv3_cWKToZ6mbeQo9cpcAAAFwM0q3DQ";
//
//        // when
//        Long socialUserId = socialTokenService.querySocialUserId(accessToken);
//
//        // then
//        assertThat(socialUserId).isEqualTo(validatedUserId);
//    }
//}
