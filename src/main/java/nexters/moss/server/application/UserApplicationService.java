package nexters.moss.server.application;

import lombok.AllArgsConstructor;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.UserLogin;
import nexters.moss.server.domain.model.exception.SocialUserInfoException;
import nexters.moss.server.domain.service.SocialTokenService;
import nexters.moss.server.domain.service.TokenService;
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
    public Response join(String accessToken, String nickname) {
        Long socialId = socialTokenService.getSocialUserId(accessToken);

        if (userRepository.existsBySocialId(socialId)) {
            throw new DuplicateKeyException("Duplicated Social ID User");
        }

        User newUser = userRepository.save(
                User.builder()
                        .socialId(socialId)
                        .nickname(nickname)
                        .build()
        );

        return new Response();
    }

    @Transactional
    public Response<UserLogin> login(String accessToken) {
        Long socialId = socialTokenService.getSocialUserId(accessToken);
        User user = userRepository.findBySocialId(socialId).orElseThrow(() -> new SocialUserInfoException("No Matched User"));

        String accountToken = tokenService.createToken(user.getId(), accessToken);
        user.setAccountToken(accountToken);
        User updatedUser = userRepository.save(user);

        return new Response<>(
                new UserLogin(updatedUser.getId(), updatedUser.getAccountToken())
        );
    }
}
