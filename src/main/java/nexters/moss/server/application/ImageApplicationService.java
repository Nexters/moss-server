package nexters.moss.server.application;

import nexters.moss.server.application.dto.S3;
import nexters.moss.server.domain.value.HabitType;
import nexters.moss.server.application.value.ImageEvent;
import org.springframework.stereotype.Service;

@Service
public class ImageApplicationService {
    private final String GIF = "gif";
    private final String PNG = "png";
    private S3 s3;

    public ImageApplicationService(S3 s3) {
        this.s3 = s3;
    }

    public String getMoveImagePath(HabitType habitType, ImageEvent event) {
        return imageBaseUrlBuilder(habitType, event) + "." + GIF;
    }

    public String getWholeDiaryImagePath(HabitType habitType, ImageEvent event) {
        return imageBaseUrlBuilder(habitType, event) + "." + PNG;
    }

    public String getPieceDiaryImagePath(HabitType habitType, ImageEvent event, int count) {
        return imageBaseUrlBuilder(habitType, event) + "_" + count + "." + PNG;
    }

    private String imageBaseUrlBuilder(HabitType habitType, ImageEvent event) {
        StringBuilder stringBuilder = new StringBuilder(s3.getUrl());
        return stringBuilder
                .append("/")
                .append(habitType.getKey())
                .append("/")
                .append(event.getName())
                .toString();
    }
}
