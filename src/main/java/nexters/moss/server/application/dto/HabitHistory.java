package nexters.moss.server.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.value.Habit;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HabitHistory {
    @JsonProperty("id")
    private Long habitId;
    @JsonProperty("name")
    private Habit habitName;
    @JsonProperty("records")
    private List<HabitRecord> habitRecords;
}
