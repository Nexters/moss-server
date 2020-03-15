package nexters.moss.server.user;

import nexters.moss.server.domain.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {

    }

    @Test
    public void userJoinTest_shouldSuccess() {

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
