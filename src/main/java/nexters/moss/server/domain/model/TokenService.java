package nexters.moss.server.domain.model;

public interface TokenService {
    public String createToken(Long userId, String accessToken);
    public String createToken(Long userId, String accessToken, String accountToken);
    public Token recoverToken(String accountToken);
}
