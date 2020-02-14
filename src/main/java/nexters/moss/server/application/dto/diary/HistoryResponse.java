package nexters.moss.server.application.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryResponse {
    private String habitName;
    private String description;
    private String cakeName;
    private List<LocalDateTime> dates;
}
