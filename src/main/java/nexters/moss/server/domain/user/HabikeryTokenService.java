package nexters.moss.server.domain.user;

public interface HabikeryTokenService {
    public String createToken(Long userId, String accessToken);
    public Token recoverToken(String habikeryToken);
}
