package nexters.moss.server.domain.user;

import nexters.moss.server.config.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabikeryTokenService habikeryTokenService;

    public Long authenticate(String habikeryToken) {
        long userId = habikeryTokenService.recoverToken(habikeryToken);

        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("Not joined User"));

        if(!user.getHabikeryToken().equals(habikeryToken)) {
            throw new UnauthorizedException("Unauthorized Token");
        }

        return user.getId();
    }
}
