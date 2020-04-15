package nexters.moss.server.application;

import nexters.moss.server.application.dto.HabitCheckResponse;
import nexters.moss.server.application.dto.HabitDoneResponse;
import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.NewCakeDTO;
import nexters.moss.server.application.value.ImageEvent;
import nexters.moss.server.config.exception.AlreadyExistException;
import nexters.moss.server.config.exception.ResourceNotFoundException;
import nexters.moss.server.config.exception.UnauthorizedException;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.domain.value.CategoryType;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.HabitRepository;
import nexters.moss.server.domain.repository.UserRepository;
import nexters.moss.server.domain.service.HabitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HabitApplicationService {
    private HabitRepository habitRepository;
    private HabitService habitService;
    private UserRepository userRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;
    private WholeCakeRepository wholeCakeRepository;
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;
    private ImageApplicationService imageApplicationService;
    private CategoryApplicationService categoryApplicationService;

    public HabitApplicationService(
            HabitRepository habitRepository,
            HabitService habitService,
            UserRepository userRepository,
            PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository,
            WholeCakeRepository wholeCakeRepository,
            PieceOfCakeSendRepository pieceOfCakeSendRepository,
            ImageApplicationService imageApplicationService,
            CategoryApplicationService categoryApplicationService
    ) {
        this.habitRepository = habitRepository;
        this.habitService = habitService;
        this.userRepository = userRepository;
        this.pieceOfCakeReceiveRepository = pieceOfCakeReceiveRepository;
        this.wholeCakeRepository = wholeCakeRepository;
        this.pieceOfCakeSendRepository = pieceOfCakeSendRepository;
        this.imageApplicationService = imageApplicationService;
        this.categoryApplicationService = categoryApplicationService;
    }

    public Response<HabitHistory> createHabit(Long userId, Long categoryId) {
        // TODO: user exist validation
        CategoryType category = categoryApplicationService.findById(categoryId);

        if (habitRepository.existsByUserIdAndCategoryId(userId, categoryId)) {
            throw new IllegalArgumentException("Already Has Habit");
        }
        int habitCount = habitRepository.countAllByUserId(userId);
        Habit habit = habitRepository.save(
                Habit.builder()
                        .userId(userId)
                        .categoryId(categoryId)
                        .order(habitCount)
                        .build()
        );
        habit.onActivation();
        return new Response(
                new HabitHistory(
                        habit.getId(),
                        habit.getCategoryId(),
                        category.getHabitType(),
                        habit.getIsFirstCheck(),
                        habit.getHabitRecords()
                )
        );
    }

    public Response<List<HabitHistory>> getHabit(Long userId) {
        List<Habit> habits = habitRepository.findAllByUserId(userId);

        return new Response<>(
                habits
                        .stream()
                        .map(habit -> {
                                    habit.refreshHabitHistory();
                                    CategoryType category = categoryApplicationService.findById(habit.getCategoryId());
                                    return new HabitHistory(
                                            habit.getId(),
                                            habit.getCategoryId(),
                                            category.getHabitType(),
                                            habit.getIsFirstCheck(),
                                            habit.getHabitRecords()
                                    );
                                }
                        )
                        .collect(Collectors.toList())
        );
    }

    public Response<Long> deleteHabit(Long userId, Long habitId) {
        List<Habit> habits = habitRepository.findAllByUserIdOrderByOrderAsc(userId);
        Habit habit = findHabitById(habits, habitId);
        habitService.refreshHabitsOrderWhenDelete(habits, habit.getOrder());
        habits.remove(habit);
        habitRepository.deleteById(habitId);
        habitRepository.saveAll(habits);
        return new Response<>(habitId);
    }

    // TODO: separate done and receive logic
    // TODO: separate jpa consistence context
    public HabitDoneResponse doneHabit(Long userId, Long habitId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User"));
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new ResourceNotFoundException("No Matched Habit"));
        CategoryType category = categoryApplicationService.findById(habit.getCategoryId());
        if (habit.isDone()) {
            throw new AlreadyExistException("Already done habit");
        }
        habit.count();
        habit.offFirstCheck();
        if (habit.isFirstCheck()) {
            habit.onFirstCheck();
        }
        habit.done();
        habit.refreshHabitHistory();
        habitRepository.save(habit);

        if (!habit.isReadyToReceiveCake()) {
            return new HabitDoneResponse(
                    new HabitCheckResponse(
                            habit.getId(),
                            category.getHabitType(),
                            habit.getIsFirstCheck(),
                            habit.getHabitRecords(),
                            habit.getCategoryId()
                    ), null
            );
        }
        SentPieceOfCake sentPieceOfCake = pieceOfCakeSendRepository.findRandomByUser_IdAndCategoryId(userId, habit.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Has no remain cake message"));

        int pieceCount = pieceOfCakeReceiveRepository.countAllByUser_IdAndCategoryId(userId, habit.getCategoryId());

        pieceOfCakeReceiveRepository.save(
                ReceivedPieceOfCake.builder()
                        .user(user)
                        .categoryId(category.getId())
                        .sentPieceOfCake(sentPieceOfCake)
                        .build()
        );

        if ((pieceCount + 1) % 8 == 0) {
            wholeCakeRepository.save(new WholeCake(
                    userId,
                    habitId,
                    category.getId()
            ));
        }

        return new HabitDoneResponse(
                new HabitCheckResponse(
                        habit.getId(),
                        category.getHabitType(),
                        habit.getIsFirstCheck(),
                        habit.getHabitRecords(),
                        habit.getCategoryId()
                ),
                new NewCakeDTO(
                        user.getNickname(),
                        sentPieceOfCake.getNote(),
                        category.getCakeType().getName(),
                        imageApplicationService.getMoveImagePath(category.getHabitType(), ImageEvent.NEW_CAKE))
        );
    }

    public Response<Long> changeHabitOrder(Long userId, Long habitId, int changedOrder) {
        List<Habit> habits = habitRepository.findAllByUserIdOrderByOrderAsc(userId);
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

    @Transactional(readOnly = true)
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User"));
    }

    @Transactional(readOnly = true)
    public Habit findHabitById(Long habitId) {
        return habitRepository.findById(habitId).orElseThrow(() -> new ResourceNotFoundException("No Matched Habit"));
    }

    private Habit findHabitById(List<Habit> habits, Long habitId) {
        for (Habit habit : habits) {
            if (habit.getId().equals(habitId)) {
                return habit;
            }
        }
        return null;
    }
}
