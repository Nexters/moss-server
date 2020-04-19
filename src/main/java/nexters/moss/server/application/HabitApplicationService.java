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
import nexters.moss.server.domain.Category;
import nexters.moss.server.domain.cake.*;
import nexters.moss.server.domain.habit.Habit;
import nexters.moss.server.domain.user.User;
import nexters.moss.server.domain.habit.HabitRepository;
import nexters.moss.server.domain.user.UserRepository;
import nexters.moss.server.domain.habit.HabitService;
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
    private ReceivedPieceOfCakeRepository receivedPieceOfCakeRepository;
    private WholeCakeRepository wholeCakeRepository;
    private SentPieceOfCakeRepository sentPieceOfCakeRepository;
    private ImageApplicationService imageApplicationService;
    private CategoryApplicationService categoryApplicationService;

    public HabitApplicationService(
            HabitRepository habitRepository,
            HabitService habitService,
            UserRepository userRepository,
            ReceivedPieceOfCakeRepository receivedPieceOfCakeRepository,
            WholeCakeRepository wholeCakeRepository,
            SentPieceOfCakeRepository sentPieceOfCakeRepository,
            ImageApplicationService imageApplicationService,
            CategoryApplicationService categoryApplicationService
    ) {
        this.habitRepository = habitRepository;
        this.habitService = habitService;
        this.userRepository = userRepository;
        this.receivedPieceOfCakeRepository = receivedPieceOfCakeRepository;
        this.wholeCakeRepository = wholeCakeRepository;
        this.sentPieceOfCakeRepository = sentPieceOfCakeRepository;
        this.imageApplicationService = imageApplicationService;
        this.categoryApplicationService = categoryApplicationService;
    }

    public Response<HabitHistory> createHabit(Long userId, Long categoryId) {
        // TODO: user exist validation
        Category category = categoryApplicationService.findById(categoryId);

        if (habitRepository.existsByUserIdAndCategoryId(userId, categoryId)) {
            throw new IllegalArgumentException("Already Has Habit");
        }
        int habitCount = habitRepository.countAllByUserId(userId);
        Habit habit = habitRepository.save(
                Habit.builder()
                        .userId(userId)
                        .categoryId(categoryId)
                        .build()
        );
        habit.changeOrder(habitCount);
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
                                    Category category = categoryApplicationService.findById(habit.getCategoryId());
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

    public HabitDoneResponse doneHabit(Long userId, Long habitId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User"));
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new ResourceNotFoundException("No Matched Habit"));
        Category category = categoryApplicationService.findById(habit.getCategoryId());
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

        return new HabitDoneResponse(
                new HabitCheckResponse(
                        habit.getId(),
                        category.getHabitType(),
                        habit.getIsFirstCheck(),
                        habit.getHabitRecords(),
                        habit.getCategoryId()
                ),
                habit.isReadyToReceiveCake() ? processSendPieceOfCake(user, category, habit) : null
        );

    }

    private NewCakeDTO processSendPieceOfCake(User user, Category category, Habit habit) {
        SentPieceOfCake sentPieceOfCake = sentPieceOfCakeRepository.findRandomByUserIdAndCategoryId(user.getId(), category.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Has no remain cake message"));

        int pieceCount = receivedPieceOfCakeRepository.countAllByUserIdAndCategoryId(user.getId(), category.getId());

        receivedPieceOfCakeRepository.save(
                ReceivedPieceOfCake.builder()
                        .userId(user.getId())
                        .categoryId(category.getId())
                        .sentPieceOfCakeId(sentPieceOfCake.getId())
                        .build()
        );

        if ((pieceCount + 1) % 8 == 0) {
            wholeCakeRepository.save(new WholeCake(
                    user.getId(),
                    habit.getId(),
                    category.getId()
            ));
        }

        return new NewCakeDTO(
                user.getNickname(),
                sentPieceOfCake.getNote(),
                category.getCakeType().getName(),
                imageApplicationService.getMoveImagePath(category.getHabitType(), ImageEvent.NEW_CAKE)
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
