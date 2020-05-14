package nexters.moss.server.user;

import nexters.moss.server.TestConfiguration;
import nexters.moss.server.domain.user.User;
import nexters.moss.server.domain.user.UserRepository;
import nexters.moss.server.domain.user.AuthenticationService;
import nexters.moss.server.domain.user.HabikeryTokenService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class AuthenticationServiceTest {
    @Autowired
    private TestConfiguration testConfiguration;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private HabikeryTokenService habikeryTokenService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        User user = userRepository.save(
                User.builder()
                        .socialId(12345678L)
                        .nickname("testUserNickname")
                        .build()
        );

        String testHabikeryToken = habikeryTokenService.createToken(user.getId());
        user.setHabikeryToken(testHabikeryToken);
        userRepository.save(user);
    }

    @After
    public void tearDown() {
        testConfiguration.tearDown();
    }

    @Test(expected = Test.None.class)
    public void authenticateTest() {
        List<User> userList = userRepository.findAll();
        User testUser = userList.get(userList.size() - 1);
        Long resultUserId = authenticationService.authenticate(testUser.getHabikeryToken());

        assertThat(resultUserId).isEqualTo(testUser.getId());
    }
}
