package nexters.moss.server.application;

import lombok.AllArgsConstructor;
import nexters.moss.server.domain.model.SocialTokenService;
import nexters.moss.server.domain.model.TokenService;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserApplicationService {
    private SocialTokenService socialTokenService;
    private TokenService tokenService;
    private UserRepository userRepository;

    @Transactional
    public void join(String accessToken, String nickname) {
        Long socialId = socialTokenService.querySocialUserId(accessToken);

        if(userRepository.findBySocialId(socialId).isPresent()) {
            throw new DuplicateKeyException("Duplicated Social ID User");
        }

        User newUser = User.builder()
                .socialId(socialId)
                .nickname(nickname)
                .build();

        userRepository.save(newUser);
    }
}
