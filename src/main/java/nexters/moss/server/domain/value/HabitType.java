package nexters.moss.server.domain.value;

import lombok.Getter;

@Getter
public enum HabitType {
    WATER("water", "물마시기"),
    STRETCHING("stretching","스트레칭"),
    MEDITATION("meditation", "명상"),
    WALK("walk","산책"),
    NEWS("news","뉴스보기"),
    BREAKFAST("breakfast","아침식사"),
    DIARY("diary","일기쓰기"),
    READING("reading","책읽기");

    private String key;
    private String name;

    HabitType(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
