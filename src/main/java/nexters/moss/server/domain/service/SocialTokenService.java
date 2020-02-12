package nexters.moss.server.domain.service;

import nexters.moss.server.domain.model.exception.NoSocialUserInfoException;

public interface SocialTokenService {
    public Long querySocialUserId(String accessToken) throws NoSocialUserInfoException;
}
