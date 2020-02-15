package nexters.moss.server.domain.service;

import nexters.moss.server.domain.model.exception.UserInfoException;

public interface SocialTokenService {
    public Long getSocialUserId(String accessToken) throws UserInfoException;
}
