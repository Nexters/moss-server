package nexters.moss.server.config.exception;

public class AlreadyExistException extends RuntimeException  {
    public AlreadyExistException(String message) {
        super(message);
    }
}
