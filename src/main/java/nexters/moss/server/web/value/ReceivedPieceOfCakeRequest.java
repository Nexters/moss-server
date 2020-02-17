package nexters.moss.server.web.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceivedPieceOfCakeRequest {
    private Long receivedPieceOfCakeId;
    private String reason;
}
