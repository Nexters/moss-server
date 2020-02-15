package nexters.moss.server.user;

import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.UserRepository;
import nexters.moss.server.domain.service.AuthenticationService;
import nexters.moss.server.domain.service.HabikeryTokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class AuthenticationServiceTest {
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

        String testHabikeryToken = habikeryTokenService.createToken(user.getId(), "accessToken");
        userRepository.save(new User(
                user.getId(),
                user.getSocialId(),
                testHabikeryToken,
                user.getNickname(),
                null,
                null,
                null,
                null
        ));
    }

    @Test(expected = Test.None.class)
    public void authenticateTest() {
        User testUser = userRepository.findAll().get(0);
        authenticationService.authenticate(testUser.getHabikeryToken());
    }
}
