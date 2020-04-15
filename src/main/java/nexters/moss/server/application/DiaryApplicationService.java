package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.diary.DiaryDTO;
import nexters.moss.server.application.dto.diary.HistoryResponse;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.application.value.ImageEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiaryApplicationService {
    private WholeCakeRepository wholeCakeRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;
    private HabitRepository habitRepository;
    private ImageApplicationService imageApplicationService;
    private CategoryApplicationService categoryApplicationService;

    public DiaryApplicationService(
            WholeCakeRepository wholeCakeRepository,
            PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository,
            HabitRepository habitRepository,
            ImageApplicationService imageApplicationService,
            CategoryApplicationService categoryApplicationService
    ) {
        this.wholeCakeRepository = wholeCakeRepository;
        this.pieceOfCakeReceiveRepository = pieceOfCakeReceiveRepository;
        this.habitRepository = habitRepository;
        this.imageApplicationService = imageApplicationService;
        this.categoryApplicationService = categoryApplicationService;
    }

    public Response<List<DiaryDTO>> getPieceOfCakeDiary(Long userId) {
        List<Habit> habits = habitRepository.findAllByUserId(userId);
        return new Response<>(
                habits
                        .stream()
                        .map(habit -> {
                                    Category category = categoryApplicationService.findById(habit.getCategoryId());
                                    return new DiaryDTO(
                                            category.getHabitType().getName(),
                                            category.getCakeType().getName(),
                                            category.getDiaryDescription().getMessage(),
                                            pieceOfCakeReceiveRepository.countAllByUser_IdAndCategoryId(userId, category.getId()) % 8,
                                            imageApplicationService.getPieceDiaryImagePath(
                                                    category.getHabitType(),
                                                    ImageEvent.PIECE_OF_CAKE_DIARY,
                                                    pieceOfCakeReceiveRepository.countAllByUser_IdAndCategoryId(userId, category.getId())
                                            )
                                    );
                                }
                        )
                        .collect(Collectors.toList())
        );
    }

    public Response<List<DiaryDTO>> getWholeCakeDiary(Long userId) {
        List<Habit> habits = habitRepository.findAllByUserId(userId);
        return new Response<>(
                habits
                        .stream()
                        .map(habit -> {
                                    Category category = categoryApplicationService.findById(habit.getCategoryId());
                                    return new DiaryDTO(
                                            category.getHabitType().getName(),
                                            category.getCakeType().getName(),
                                            category.getDiaryDescription().getMessage(),
                                            wholeCakeRepository.countAllByUser_IdAndCategoryId(userId, category.getId()),
                                            imageApplicationService.getWholeDiaryImagePath(category.getHabitType(), ImageEvent.WHOLE_CAKE_DIARY)
                                    );
                                }
                        )
                        .collect(Collectors.toList())
        );
    }

    public Response<HistoryResponse> getCakeHistory(Long userId, Long categoryId) {
        Category category = categoryApplicationService.findById(categoryId);
        return new Response<HistoryResponse>(
                new HistoryResponse(
                        category.getHabitType().getName(),
                        category.getDiaryDescription().getMessage(),
                        category.getCakeType().getName(),
                        wholeCakeRepository.findAllByUser_IdAndCategoryId(userId, categoryId)
                                .stream()
                                .map(wholeCake -> wholeCake.getCreatedAt())
                                .collect(Collectors.toList()),
                        imageApplicationService.getMoveImagePath(category.getHabitType(), ImageEvent.HISTORY)
                )
        );
    }
}
