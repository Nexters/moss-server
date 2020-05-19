package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.diary.DiaryDTO;
import nexters.moss.server.application.dto.diary.HistoryResponse;
import nexters.moss.server.domain.Category;
import nexters.moss.server.domain.cake.ReceivedPieceOfCakeRepository;
import nexters.moss.server.domain.cake.WholeCakeRepository;
import nexters.moss.server.domain.exceptions.UnauthorizedException;
import nexters.moss.server.domain.habit.Habit;
import nexters.moss.server.domain.habit.HabitRepository;
import nexters.moss.server.application.value.ImageEvent;
import nexters.moss.server.domain.user.User;
import nexters.moss.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiaryApplicationService {
    private WholeCakeRepository wholeCakeRepository;
    private ReceivedPieceOfCakeRepository receivedPieceOfCakeRepository;
    private HabitRepository habitRepository;
    private ImageApplicationService imageApplicationService;
    private CategoryApplicationService categoryApplicationService;
    private UserRepository userRepository;

    public DiaryApplicationService(
            WholeCakeRepository wholeCakeRepository,
            ReceivedPieceOfCakeRepository receivedPieceOfCakeRepository,
            HabitRepository habitRepository,
            ImageApplicationService imageApplicationService,
            CategoryApplicationService categoryApplicationService,
            UserRepository userRepository
    ) {
        this.wholeCakeRepository = wholeCakeRepository;
        this.receivedPieceOfCakeRepository = receivedPieceOfCakeRepository;
        this.habitRepository = habitRepository;
        this.imageApplicationService = imageApplicationService;
        this.categoryApplicationService = categoryApplicationService;
        this.userRepository = userRepository;
    }

    public Response<List<DiaryDTO>> getPieceOfCakeDiary(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User"));
        return new Response<>(
                user.getCheckList()
                        .stream()
                        .map(categoryId -> {
                                    Category category = categoryApplicationService.findById(categoryId);
                                    return new DiaryDTO(
                                            category.getHabitType().getName(),
                                            category.getCakeType().getName(),
                                            category.getDiaryDescription().getMessage(),
                                            receivedPieceOfCakeRepository.countAllByUserIdAndCategoryId(userId, category.getId()) % 8,
                                            imageApplicationService.getPieceDiaryImagePath(
                                                    category.getHabitType(),
                                                    ImageEvent.PIECE_OF_CAKE_DIARY,
                                                    receivedPieceOfCakeRepository.countAllByUserIdAndCategoryId(userId, category.getId())
                                            )
                                    );
                                }
                        )
                        .collect(Collectors.toList())
        );
    }

    public Response<List<DiaryDTO>> getWholeCakeDiary(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User"));
        return new Response<>(
                user.getCheckList()
                        .stream()
                        .map(categoryId -> {
                                    Category category = categoryApplicationService.findById(categoryId);
                                    return new DiaryDTO(
                                            category.getHabitType().getName(),
                                            category.getCakeType().getName(),
                                            category.getDiaryDescription().getMessage(),
                                            wholeCakeRepository.countAllByUserIdAndCategoryId(userId, category.getId()),
                                            imageApplicationService.getWholeDiaryImagePath(category.getHabitType(), ImageEvent.WHOLE_CAKE_DIARY)
                                    );
                                }
                        )
                        .collect(Collectors.toList())
        );
    }

    public Response<HistoryResponse> getCakeHistory(Long userId, Long categoryId) {
        Category category = categoryApplicationService.findById(categoryId);
        return new Response<>(
                new HistoryResponse(
                        category.getHabitType().getName(),
                        category.getDiaryDescription().getMessage(),
                        category.getCakeType().getName(),
                        wholeCakeRepository.findAllByUserIdAndCategoryId(userId, categoryId)
                                .stream()
                                .map(wholeCake -> wholeCake.getCreatedAt())
                                .collect(Collectors.toList()),
                        imageApplicationService.getMoveImagePath(category.getHabitType(), ImageEvent.HISTORY)
                )
        );
    }
}
