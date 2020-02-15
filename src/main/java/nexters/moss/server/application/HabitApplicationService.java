package nexters.moss.server.application;

import nexters.moss.server.application.dto.HabitDoneResponse;
import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.NewCakeDTO;
import nexters.moss.server.application.value.ImageEvent;
import nexters.moss.server.config.exception.ResourceNotFoundException;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.config.exception.HabikeryUserNotFoundException;
import nexters.moss.server.domain.model.Category;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.CategoryRepository;
import nexters.moss.server.domain.repository.HabitRecordRepository;
import nexters.moss.server.domain.repository.HabitRepository;
import nexters.moss.server.domain.repository.UserRepository;
import nexters.moss.server.domain.service.HabitRecordService;
import nexters.moss.server.domain.service.HabitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HabitApplicationService {
    private CategoryRepository categoryRepository;
    private HabitRepository habitRepository;
    private HabitRecordRepository habitRecordRepository;
    private HabitRecordService habitRecordService;
    private HabitService habitService;
    private UserRepository userRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;
    private WholeCakeRepository wholeCakeRepository;
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;
    private ImageApplicationService imageApplicationService;

    public HabitApplicationService(
            CategoryRepository categoryRepository,
            HabitRepository habitRepository,
            HabitRecordRepository habitRecordRepository,
            HabitRecordService habitRecordService,
            HabitService habitService,
            UserRepository userRepository,
            PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository,
            WholeCakeRepository wholeCakeRepository,
            PieceOfCakeSendRepository pieceOfCakeSendRepository,
            ImageApplicationService imageApplicationService
    ) {
        this.categoryRepository = categoryRepository;
        this.habitRepository = habitRepository;
        this.habitRecordRepository = habitRecordRepository;
        this.habitRecordService = habitRecordService;
        this.habitService = habitService;
        this.userRepository = userRepository;
        this.pieceOfCakeReceiveRepository = pieceOfCakeReceiveRepository;
        this.wholeCakeRepository = wholeCakeRepository;
        this.pieceOfCakeSendRepository = pieceOfCakeSendRepository;
        this.imageApplicationService = imageApplicationService;
    }

    public Response<HabitHistory> createHabit(Long userId, Long categoryId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new HabikeryUserNotFoundException("No Matched User"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("No Matched Category"));
        if (habitRepository.existsByUser_IdAndCategory_Id(userId, categoryId)) {
            throw new IllegalArgumentException("Already Has Habit");
        }
        Habit habit = habitRepository.save(
                Habit.builder()
                        .user(user)
                        .category(category)
                        .isActivated(false)
                        .isFirstCheck(false)
                        .build()
        );
        List<HabitRecord> habitRecords = habitRecordService.createHabitRecords(user, habit);
        habitRecords = habitRecordRepository.saveAll(habitRecords);
        habit.onActivation();
        return new Response(
                new HabitHistory(
                        habit.getId(),
                        category.getHabitType(),
                        habit.getIsFirstCheck(),
                        habitRecords
                )
        );
    }

    public Response<List<HabitHistory>> getHabitHistory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new HabikeryUserNotFoundException("No Matched User"));

        return new Response<>(
                user.getHabits()
                        .stream()
                        .map(habit -> {
                                    List<HabitRecord> habitRecords = habit.getHabitRecords();
                                    if (habitRecordService.refreshHabitHistoryAndReturnIsChanged(habitRecords)) {
                                        habitRecords = habitRecordRepository.saveAll(habitRecords);
                                    }
                                    return new HabitHistory(
                                            habit.getId(),
                                            habit.getCategory().getHabitType(),
                                            habit.getIsFirstCheck(),
                                            habitRecords
                                    );
                                }
                        )
                        .collect(Collectors.toList())
        );
    }

    public Response<Long> deleteHabit(Long userId, Long habitId) {
        habitRecordRepository.deleteAllByUser_IdAndHabit_Id(userId, habitId);
        habitRepository.deleteById(habitId);
        return new Response<>(habitId);
    }

    public Response<HabitDoneResponse> doneHabit(Long userId, Long habitId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new HabikeryUserNotFoundException("No Matched User"));
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new ResourceNotFoundException("No Matched Habit"));
        habitService.doDoneHabit(habit);
        habitRepository.save(habit);

        List<HabitRecord> habitRecords = habit.getHabitRecords();
        if (habitRecordService.refreshHabitHistoryAndReturnIsChanged(habitRecords)) {
            habitRecords = habitRecordRepository.saveAll(habitRecords);
        }

        SentPieceOfCake sentPieceOfCake = pieceOfCakeSendRepository.findRandomByUser_IdAndCategory_Id(userId, habit.getCategory().getId()).orElseThrow(() -> new ResourceNotFoundException("Has no remain cake message"));

        int pieceCount = pieceOfCakeReceiveRepository.countAllByUser_IdAndCategory_Id(userId, habit.getCategory().getId());

        pieceOfCakeReceiveRepository.save(
                ReceivedPieceOfCake.builder()
                        .user(user)
                        .category(habit.getCategory())
                        .sentPieceOfCake(sentPieceOfCake)
                        .build()
        );

        if ((pieceCount + 1) % 8 == 0) {
            wholeCakeRepository.save(new WholeCake(
                    userId,
                    habitId,
                    habit.getCategory().getId()
            ));
        }

        return new Response<>(
                new HabitDoneResponse(
                        habit.getId(),
                        habit.getCategory().getHabitType(),
                        habit.getIsFirstCheck(),
                        habitRecords,
                        new NewCakeDTO(
                                user.getNickname(),
                                sentPieceOfCake.getNote(),
                                habit.getCategory().getCakeType().getName(),
                                imageApplicationService.getMoveImagePath(habit.getCategory().getHabitType(), ImageEvent.NEW_CAKE))
                )
        );
    }
}
