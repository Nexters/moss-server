package nexters.moss.server.domain.value;

import lombok.Getter;

@Getter
public enum CakeType {
    WATERMELON("WATERMELON", "수박"),
    CHEESE("CHEESE", "치즈"),
    WHIPPING_CREAM("WHIPPING_CREAM", "생크림"),
    GREEN_TEA("GREEN_TEA", "녹차"),
    COFFEE("COFFEE", "커피"),
    APPLE("APPLE", "애플"),
    CHESTNUT("CHESTNUT", "밤"),
    WALNUT("WALNUT", "아몬드");

    private String key;
    private String name;

    CakeType(String key, String name) {
        this.key = key;
        this.name = name;
    }
}