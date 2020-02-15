package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.diary.DiaryDTO;
import nexters.moss.server.application.dto.diary.HistoryResponse;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
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

    public DiaryApplicationService(
            WholeCakeRepository wholeCakeRepository,
            DescriptionRepository descriptionRepository,
            PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository
    ) {
        this.wholeCakeRepository = wholeCakeRepository;
        this.descriptionRepository = descriptionRepository;
        this.pieceOfCakeReceiveRepository = pieceOfCakeReceiveRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Response<List<DiaryDTO>> getPieceOfCakeDiary(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User Id"));
        return new Response<>(
                user.getHabits()
                        .stream()
                        .map(habit ->
                                new DiaryDTO(
                                        habit.getCategory().getHabitType().getName(),
                                        habit.getCategory().getCakeType().getName(),
                                        descriptionRepository.findByCategory_Id(habit.getCategory().getId()).getDiary(),
                                        pieceOfCakeReceiveRepository.countAllByUser_IdAndCategory_Id(userId, habit.getCategory().getId())
                                )
                        )
                        .collect(Collectors.toList())
        );
    }

    public Response<List<DiaryDTO>> getWholeCakeDiary(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User Id"));
        return new Response<>(
                user.getHabits()
                        .stream()
                        .map(habit ->
                                new DiaryDTO(
                                        habit.getCategory().getHabitType().getName(),
                                        habit.getCategory().getCakeType().getName(),
                                        descriptionRepository.findByCategory_Id(habit.getCategory().getId()).getDiary(),
                                        wholeCakeRepository.countAllByUser_IdAndCategory_Id(user.getId(), habit.getCategory().getId())
                                )
                        )
                        .collect(Collectors.toList())
        );
    }

    public Response<HistoryResponse> getCakeHistory(Long userId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("No Matched Category Id"));
        return new Response<HistoryResponse>(
                new HistoryResponse(
                        category.getHabitType().getName(),
                        descriptionRepository.findByCategory_Id(categoryId).getCakeHistory(),
                        category.getCakeType().getName(),
                        wholeCakeRepository.findAllByUser_IdAndCategory_Id(userId, categoryId)
                                .stream()
                                .map(wholeCake -> wholeCake.getCreatedAt())
                                .collect(Collectors.toList())
                )
        );
    }
}
