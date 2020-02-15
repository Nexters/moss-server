package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.diary.DiaryDTO;
import nexters.moss.server.application.dto.diary.HistoryResponse;
import nexters.moss.server.domain.model.Category;
import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.model.WholeCake;
import nexters.moss.server.domain.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryApplicationService {
    private WholeCakeRepository wholeCakeRepository;
    private DescriptionRepository descriptionRepository;
    private HabitRecordRepository habitRecordRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    public DiaryApplicationService(WholeCakeRepository wholeCakeRepository, DescriptionRepository descriptionRepository, HabitRecordRepository habitRecordRepository, PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.wholeCakeRepository = wholeCakeRepository;
        this.descriptionRepository = descriptionRepository;
        this.habitRecordRepository = habitRecordRepository;
        this.pieceOfCakeReceiveRepository = pieceOfCakeReceiveRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Response<List<DiaryDTO>> getPieceOfCakeDiary(Long userId){
        List<HabitRecord> habits = habitRecordRepository.findAllByUser(userRepository.findById(userId).get());
        List<DiaryDTO> diaries = new ArrayList<>();

        for(HabitRecord habit : habits){
            diaries.add(new DiaryDTO(habit.getHabit().getCategory().getHabitType().getName()
                                    ,habit.getHabit().getCategory().getCakeType().getName()
                                    ,descriptionRepository.findByCategory(habit.getHabit().getCategory()).getDiary()
                                    ,pieceOfCakeReceiveRepository.countAllByUserAndCategory(userRepository.findById(userId).get(),habit.getHabit().getCategory()) ,null));
        }
        return new Response<List<DiaryDTO>>(diaries);
    }

    public Response<List<DiaryDTO>> getWholeCakeDiary(Long userId){
        List<HabitRecord> habits = habitRecordRepository.findAllByUser(userRepository.findById(userId).get());
        List<DiaryDTO> diaries = new ArrayList<>();

        User user = userRepository.findById(userId).get();
        for(HabitRecord habit : habits){
            diaries.add(new DiaryDTO(habit.getHabit().getCategory().getHabitType().getName()
                    ,habit.getHabit().getCategory().getCakeType().getName()
                    ,descriptionRepository.findByCategory(habit.getHabit().getCategory()).getDiary()
                    ,wholeCakeRepository.countAllByUserAndCategory(user, habit.getHabit().getCategory()) ,null));
        }
        return new Response<List<DiaryDTO>>(diaries);
    }

    public Response<HistoryResponse> getCakeHistory(Long userId, Long categoryId){
        User user = userRepository.findById(userId).get();
        Category category = categoryRepository.findById(categoryId).get();

        List<WholeCake> wholeCakeList = wholeCakeRepository.findAllByUserAndCategory(user, category);

        List<LocalDateTime> dates = new ArrayList<>();
        for(WholeCake wholeCake : wholeCakeList){
            dates.add(wholeCake.getCreatedAt());
        }

        return new Response<HistoryResponse>(new HistoryResponse(category.getHabitType().getName(),
                                                                descriptionRepository.findByCategory(category).getCakeHistory(),
                                                                category.getCakeType().getName(),
                                                                dates));
    }
}
