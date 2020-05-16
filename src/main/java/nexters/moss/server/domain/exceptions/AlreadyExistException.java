package nexters.moss.server.domain.exceptions;

public class AlreadyExistException extends RuntimeException  {
    public AlreadyExistException(String message) {
        super(message);
    }
}
