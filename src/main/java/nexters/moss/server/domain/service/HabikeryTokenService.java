package nexters.moss.server.domain.service;

import nexters.moss.server.domain.model.Token;

public interface HabikeryTokenService {
    public String createToken(Long userId, String accessToken);
    public String createToken(Long userId, String accessToken, String habikeryToken);
    public Token recoverToken(String habikeryToken);
}
