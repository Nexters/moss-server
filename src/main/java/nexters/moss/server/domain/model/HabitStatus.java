package nexters.moss.server.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum HabitStatus {
    DONE("DONE"),
    NOT_DONE("NOT_DONE"),
    CAKE_DONE("CAKE_DONE"),
    CAKE_NOT_DONE("CAKE_NOT_DONE");

    private String status;

    HabitStatus(String status) {
        this.status = status;
    }
}
