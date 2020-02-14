package nexters.moss.server.application.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaryDTO {
    private String habitName;
    private String description;
    private String cakeName;
    private int count;
    private String imagePath;
}
