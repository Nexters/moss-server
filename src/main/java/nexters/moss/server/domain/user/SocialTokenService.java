package nexters.moss.server.domain.user;

import nexters.moss.server.config.exception.UnauthorizedException;

public interface SocialTokenService {
    public Long getSocialUserId(String accessToken) throws UnauthorizedException;
}
