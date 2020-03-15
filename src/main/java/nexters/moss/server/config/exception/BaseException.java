package nexters.moss.server.config.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {
    protected int code;

    public BaseException(int code, String message) {
        super(message);

        this.code = code;
    }
}
