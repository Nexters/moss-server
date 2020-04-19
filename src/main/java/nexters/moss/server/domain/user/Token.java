package nexters.moss.server.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Token {
    private Long userId;
    private String accessToken;
    private String habikeryToken;
}
