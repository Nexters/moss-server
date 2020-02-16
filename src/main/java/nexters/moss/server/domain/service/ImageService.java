package nexters.moss.server.domain.service;

import nexters.moss.server.domain.value.HabitType;
import nexters.moss.server.domain.value.ImageEvent;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    private String url;
    public ImageService() {
        url = "nexters-habikery-image.s3.ap-northeast-2.amazonaws.com/";
    }

    public String getMoveImagePath(HabitType habitType, ImageEvent event){
        return url+
                habitType.getKey()+
                "/"+event.getName()+
                ".gif";
    }

    public String getDiaryImagePath(HabitType habitType, ImageEvent event, int count){
        return url+
                habitType.getKey()+
                "/"+event.getName()+
                "_"+count+
                ".png";
    }
}
