package nexters.moss.server.domain.service;

import nexters.moss.server.domain.model.Token;

public interface TokenService {
    public String createToken(Long userId, String accessToken);
    public String createToken(Long userId, String accessToken, String accountToken);
    public Token recoverToken(String accountToken);
}
