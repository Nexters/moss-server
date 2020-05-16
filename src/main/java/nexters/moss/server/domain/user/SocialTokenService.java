package nexters.moss.server.domain.user;

import nexters.moss.server.domain.exceptions.UnauthorizedException;

public interface SocialTokenService {
    public Long getSocialUserId(String accessToken) throws UnauthorizedException;
}
