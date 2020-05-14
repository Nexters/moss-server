package nexters.moss.server.domain.user;

public interface HabikeryTokenService {
    public String createToken(Long userId);
    public long recoverToken(String habikeryToken);
}
