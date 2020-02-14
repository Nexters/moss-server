package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.diary.DiaryDTO;
import nexters.moss.server.application.dto.diary.HistoryResponse;
import nexters.moss.server.domain.repository.WholeCakeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaryApplicationService {
    private WholeCakeRepository wholeCakeRepository;

    public DiaryApplicationService(WholeCakeRepository wholeCakeRepository) {
        this.wholeCakeRepository = wholeCakeRepository;
    }

    public Response<List<DiaryDTO>> getAPieceOfCakeDiary(Long userId){
        return null;
    }

    public Response<List<DiaryDTO>> getWholeCakeDiary(Long userId){
        return null;
    }

    public Response<HistoryResponse> getCakeHistory(Long userId, Long categoryId){
//            List<WholeCake> wholeCakeList = wholeCakeRepository.findAllByUserAndCategory(userId, categoryId);
//
//        Habit habit = wholeCakeList.get(0).getHabit();
//        List<LocalDateTime> dates = new ArrayList<>();
//        for(WholeCake wholeCake : wholeCakeList){
//            dates.add(wholeCake.getCreatedAt());
//        }
//
//        return new Response<HistoryResponse>(new HistoryResponse(habit.getHabitType().getName(),
//                "description",
//                habit.getCakeType().getName(),
//                dates));
        return null;
    }
}
