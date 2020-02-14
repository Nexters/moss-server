package nexters.moss.server.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
    @JsonProperty("id")
    private Long userId;
    @JsonProperty("accountToken")
    private String accountToken;
}
