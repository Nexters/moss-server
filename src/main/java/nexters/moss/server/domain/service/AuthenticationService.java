package nexters.moss.server.domain.service;

import nexters.moss.server.config.exception.UnauthorizedException;
import nexters.moss.server.domain.model.Token;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabikeryTokenService habikeryTokenService;

    public Long authenticate(String habikeryToken) {
        Token token = habikeryTokenService.recoverToken(habikeryToken);

        User user = userRepository.findById(token.getUserId()).orElseThrow(() -> new UnauthorizedException("Not joined User"));

        if(!user.getHabikeryToken().equals(habikeryToken)) {
            throw new UnauthorizedException("Unauthorized Token");
        }

        return user.getId();
    }
}
