package nexters.moss.server.application.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private T data;
}
