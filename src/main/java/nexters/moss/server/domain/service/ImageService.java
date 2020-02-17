package nexters.moss.server.domain.service;

import nexters.moss.server.domain.value.HabitType;
import nexters.moss.server.domain.value.ImageEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    @Value("${aws.s3.url}")
    private String url;

    public String getMoveImagePath(HabitType habitType, ImageEvent event){
        return url+
                habitType.getKey()+
                "/"+event.getName()+
                ".gif";
    }

    public String getWholeDiaryImagePath(HabitType habitType, ImageEvent event){
        return url+
                habitType.getKey()+
                "/"+event.getName()+
                ".png";
    }

    public String getPieceDiaryImagePath(HabitType habitType, ImageEvent event, int count){
        return url+
                habitType.getKey()+
                "/"+event.getName()+
                "_"+count+
                ".png";
    }
}
