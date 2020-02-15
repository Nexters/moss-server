package nexters.moss.server.domain.model.exception;

import java.io.IOException;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
