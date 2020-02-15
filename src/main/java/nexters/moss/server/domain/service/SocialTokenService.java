package nexters.moss.server.domain.service;

import nexters.moss.server.config.exception.SocialUserNotFoundException;

public interface SocialTokenService {
    public Long getSocialUserId(String accessToken) throws SocialUserNotFoundException;
}
