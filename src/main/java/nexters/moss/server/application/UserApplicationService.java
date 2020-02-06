package nexters.moss.server.application;

import lombok.RequiredArgsConstructor;
import nexters.moss.server.domain.model.SocialTokenService;
import nexters.moss.server.domain.model.TokenService;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserApplicationService {
    private SocialTokenService socialTokenService;
    private TokenService tokenService;
    private UserRepository userRepository;

    @Transactional
    public void Join(String accessToken, String nickname) {
        Long socialId = socialTokenService.querySocialUserId(accessToken);

        if(isJoinedUser(socialId)) {
            throw new DuplicateKeyException("Duplicated Social ID User");
        }

        User newUser = User.builder()
                .socialId(socialId)
                .nickname(nickname)
                .build();

        userRepository.save(newUser);
    }

    private boolean isJoinedUser(Long socialId) {
        return userRepository.findBySocialId(socialId).isPresent();
    }
}
