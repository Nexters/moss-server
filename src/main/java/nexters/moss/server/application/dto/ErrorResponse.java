package nexters.moss.server.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nexters.moss.server.config.exception.BaseException;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private long code;
    private String message;

    public ErrorResponse(BaseException e) {
        this.code = e.getCode();
        this.message = e.getMessage();
    }
}
