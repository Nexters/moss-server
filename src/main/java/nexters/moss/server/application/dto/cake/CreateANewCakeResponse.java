package nexters.moss.server.application.dto.cake;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateANewCakeResponse {
    @JsonProperty("pieceOfCakeSendId")
    private long pieceOfCakeSendId;
}
