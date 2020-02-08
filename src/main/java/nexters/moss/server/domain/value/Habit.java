package nexters.moss.server.domain.value;

import lombok.Getter;

@Getter
public enum  Habit {
    WATER("물마시기"),
    STRETCHING("스트레칭"),
    MEDITATION("명상"),
    WALK("산책"),
    NEWS("뉴스보기"),
    BREAKFAST("아침식사"),
    DIARY("일기쓰기"),
    READING("책읽기");

    private String name;

    Habit(String name) {

    }
}
