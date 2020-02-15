package nexters.moss.server.config.exception;

public class SocialUserNotFoundException extends RuntimeException {
    public SocialUserNotFoundException(String message) {
        super(message);
    }
}
