package nexters.moss.server.config.exception;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(int code, String message) {
        super(code, message);
    }
}
