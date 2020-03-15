package nexters.moss.server.config.exception;

public class NotFoundException extends BaseException {
    public NotFoundException(int code, String message) {
        super(code, message);
    }
}
