package nexters.moss.server.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nexters.moss.server.application.dto.cake.NewCakeDTO;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HabitDoneResponse {
    private HabitCheckResponse habitCheckResponse;
    private NewCakeDTO newCakeDTO;
}
