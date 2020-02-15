package nexters.moss.server.domain.service;

import nexters.moss.server.domain.model.Token;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.model.exception.UnauthorizedException;
import nexters.moss.server.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabikeryTokenService habikeryTokenService;

    public void authenticate(String habikeryToken) {
        Token token = habikeryTokenService.recoverToken(habikeryToken);

        User user = userRepository.findById(token.getUserId()).orElseThrow(() -> new UnauthorizedException("Not joined User"));

        if(!user.getHabikeryToken().equals(habikeryToken)) {
            throw new UnauthorizedException("Unauthorized Token");
        }

        if(!token.getExpirationDate().isAfter(LocalDate.now())) {
            throw new UnauthorizedException("Habikery Token is expired");
        }
    }
}
