package nexters.moss.server.application.dto.cake;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateANewCakeRequest {
    private long userId;
    private long categoryId;
    private String note;
}
