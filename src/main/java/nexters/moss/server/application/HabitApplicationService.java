package nexters.moss.server.application;

import nexters.moss.server.application.dto.HabitCheckResponse;
import nexters.moss.server.application.dto.HabitDoneResponse;
import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.NewCakeDTO;
import nexters.moss.server.application.value.ImageEvent;
import nexters.moss.server.config.exception.AlreadyDoneHabitException;
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

import java.util.Comparator;
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
        int habitCount = habitRepository.countAllByUser_Id(userId);
        Habit habit = habitRepository.save(
                Habit.builder()
                        .user(user)
                        .category(category)
                        .order(habitCount)
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
                        categoryId,
                        category.getHabitType(),
                        habit.getIsFirstCheck(),
                        habitRecords
                )
        );
    }

    public Response<List<HabitHistory>> getHabit(Long userId) {
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
                                            habit.getCategory().getId(),
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

    // TODO: separate done and receive logic
    // TODO: separate jpa consistence context
    public HabitDoneResponse doneHabit(Long userId, Long habitId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new HabikeryUserNotFoundException("No Matched User"));
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new ResourceNotFoundException("No Matched Habit"));
        if (habitService.isDoneHabit(habit)) {
            throw new AlreadyDoneHabitException("Already done habit");
        }
        habitService.doDoneHabit(habit);
        habitRepository.save(habit);

        List<HabitRecord> habitRecords = habit.getHabitRecords();
        if (habitRecordService.refreshHabitHistoryAndReturnIsChanged(habitRecords)) {
            habitRecords = habitRecordRepository.saveAll(habitRecords);
        }

        if (!habitService.isReadyToReceiveCake(habit)) {
            return new HabitDoneResponse(
                    new HabitCheckResponse(
                            habit.getId(),
                            habit.getCategory().getHabitType(),
                            habit.getIsFirstCheck(),
                            habitRecords,
                            habit.getCategory().getId()
                    ), null
            );
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

        return new HabitDoneResponse(
                new HabitCheckResponse(
                        habit.getId(),
                        habit.getCategory().getHabitType(),
                        habit.getIsFirstCheck(),
                        habitRecords,
                        habit.getCategory().getId()
                ),
                new NewCakeDTO(
                        user.getNickname(),
                        sentPieceOfCake.getNote(),
                        habit.getCategory().getCakeType().getName(),
                        imageApplicationService.getMoveImagePath(habit.getCategory().getHabitType(), ImageEvent.NEW_CAKE))
        );
    }

    public Response<Long> changeHabitOrder(Long userId, Long habitId, int changedOrder) {
        List<Habit> habits = habitRepository.findAllByUser_IdOrderByOrderAsc(userId);
        if (habits.size() == 0) {
            throw new IllegalArgumentException("User has no habits");
        }

        if (habits.size() == 1) {
            throw new IllegalArgumentException("Has no item to change order");
        }

        int habitOrder = 0;

        for (Habit habit : habits) {
            if (habit.getId().equals(habitId)) {
                habitOrder = habit.getOrder();
            }
        }
        habitService.changeHabitsOrder(habits, habitOrder, changedOrder);
        habitRepository.saveAll(habits);
        return new Response<>(habitId);
    }
}
