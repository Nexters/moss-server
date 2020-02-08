package nexters.moss.server.domain.value;

import lombok.Getter;

@Getter
public enum  Cake {
    WATERMELON("수박"),
    WALNUT("호두"),
    CHEESE("치즈"),
    GREEN_TEA("녹차"),
    WHIPPING_CREAM("생크림"),
    APPLE("애플"),
    COFFEE("커피"),
    CHESTNUT("밤");

    private String name;

    Cake(String name) {
        this.name = name;
    }
}