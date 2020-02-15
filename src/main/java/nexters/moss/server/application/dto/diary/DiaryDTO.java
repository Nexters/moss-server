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
    private String cakeName;
    private String description;
    private int count;
    private String imagePath;
}
