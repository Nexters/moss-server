package nexters.moss.server.domain.value;

import lombok.Getter;

@Getter
public enum  ImageEvent {
    NEW_CAKE("newCake"),
    PIECE_OF_CAKE_DIARY("piece"),
    WHOLE_CAKE_DIARY("whole"),
    HISTORY("history");

    String name;
    ImageEvent(String name){
        this.name = name;
    }
}
