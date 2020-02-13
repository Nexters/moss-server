package nexters.moss.server.domain.service;

import nexters.moss.server.domain.model.exception.SocialUserInfoException;

public interface SocialTokenService {
    public Long getSocialUserId(String accessToken) throws SocialUserInfoException;
}
