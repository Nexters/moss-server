package nexters.moss.server.config.exception;

public class AlreadyExistException extends BaseException  {
    public AlreadyExistException(int code, String message) {
        super(code, message);
    }
}
