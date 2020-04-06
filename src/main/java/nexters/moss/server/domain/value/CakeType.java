package nexters.moss.server.domain.value;

import lombok.Getter;

@Getter
public enum CakeType {
    WATERMELON("수박"),
    CHEESE("치즈"),
    WHIPPING_CREAM("생크림"),
    GREEN_TEA("녹차"),
    COFFEE("커피"),
    APPLE("애플"),
    CHESTNUT("밤"),
    WALNUT("아몬드");

    private String name;

    CakeType(String name) {
        this.name = name;
    }
}