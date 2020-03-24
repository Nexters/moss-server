package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.diary.DiaryDTO;
import nexters.moss.server.application.dto.diary.HistoryResponse;
import nexters.moss.server.config.exception.NotFoundException;
import nexters.moss.server.config.exception.UnauthorizedException;
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
    private DescriptionRepository descriptionRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ImageApplicationService imageApplicationService;

    public DiaryApplicationService(
            WholeCakeRepository wholeCakeRepository,
            DescriptionRepository descriptionRepository,
            PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            ImageApplicationService imageApplicationService) {
        this.wholeCakeRepository = wholeCakeRepository;
        this.descriptionRepository = descriptionRepository;
        this.pieceOfCakeReceiveRepository = pieceOfCakeReceiveRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.imageApplicationService = imageApplicationService;
    }

    public Response<List<DiaryDTO>> getPieceOfCakeDiary(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User Id"));
        return new Response<>(
                user.getHabits()
                        .stream()
                        .map(habit ->
                                new DiaryDTO(
                                        habit.getCategory().getHabitType().getName(),
                                        habit.getCategory().getCakeType().getName(),
                                        descriptionRepository.findByCategory_Id(habit.getCategory().getId()).getDiary(),
                                        pieceOfCakeReceiveRepository.countAllByUser_IdAndCategory_Id(userId, habit.getCategory().getId()) % 8,
                                        imageApplicationService.getPieceDiaryImagePath(
                                                            habit.getCategory().getHabitType(),
                                                            ImageEvent.PIECE_OF_CAKE_DIARY ,
                                                            pieceOfCakeReceiveRepository.countAllByUser_IdAndCategory_Id(userId, habit.getCategory().getId())
                                        )
                                )
                        )
                        .collect(Collectors.toList())
        );
    }

    public Response<List<DiaryDTO>> getWholeCakeDiary(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User Id"));
        return new Response<>(
                user.getHabits()
                        .stream()
                        .map(habit ->
                                new DiaryDTO(
                                        habit.getCategory().getHabitType().getName(),
                                        habit.getCategory().getCakeType().getName(),
                                        descriptionRepository.findByCategory_Id(habit.getCategory().getId()).getDiary(),
                                        wholeCakeRepository.countAllByUser_IdAndCategory_Id(user.getId(), habit.getCategory().getId()),
                                        imageApplicationService.getWholeDiaryImagePath(habit.getCategory().getHabitType(), ImageEvent.WHOLE_CAKE_DIARY)
                                )
                        )
                        .collect(Collectors.toList())
        );
    }

    public Response<HistoryResponse> getCakeHistory(Long userId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("No Matched Category Id"));
        return new Response<HistoryResponse>(
                new HistoryResponse(
                        category.getHabitType().getName(),
                        descriptionRepository.findByCategory_Id(categoryId).getDiary(),
                        category.getCakeType().getName(),
                        wholeCakeRepository.findAllByUser_IdAndCategory_Id(userId, categoryId)
                                .stream()
                                .map(wholeCake -> wholeCake.getCreatedAt())
                                .collect(Collectors.toList()),
                        imageApplicationService.getMoveImagePath(category.getHabitType(), ImageEvent.HISTORY)
                )
        );
    }
}
