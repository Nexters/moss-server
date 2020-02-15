package nexters.moss.server.config.exception;

public class SocialTokenExpiredException extends RuntimeException {
    public SocialTokenExpiredException(String message) {
        super(message);
    }
}
