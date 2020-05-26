package nexters.moss.server.application;

import nexters.moss.server.application.dto.HabitCheckResponse;
import nexters.moss.server.application.dto.HabitDoneResponse;
import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.NewCakeDTO;
import nexters.moss.server.application.value.ImageEvent;
import nexters.moss.server.domain.exceptions.AlreadyExistException;
import nexters.moss.server.domain.exceptions.ResetContentsException;
import nexters.moss.server.domain.exceptions.ResourceNotFoundException;
import nexters.moss.server.domain.exceptions.UnauthorizedException;
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
    private CakeApplicationService cakeApplicationService;

    public HabitApplicationService(
            HabitRepository habitRepository,
            HabitService habitService,
            UserRepository userRepository,
            ReceivedPieceOfCakeRepository receivedPieceOfCakeRepository,
            WholeCakeRepository wholeCakeRepository,
            SentPieceOfCakeRepository sentPieceOfCakeRepository,
            ImageApplicationService imageApplicationService,
            CategoryApplicationService categoryApplicationService,
            CakeApplicationService cakeApplicationService
    ) {
        this.habitRepository = habitRepository;
        this.habitService = habitService;
        this.userRepository = userRepository;
        this.receivedPieceOfCakeRepository = receivedPieceOfCakeRepository;
        this.wholeCakeRepository = wholeCakeRepository;
        this.sentPieceOfCakeRepository = sentPieceOfCakeRepository;
        this.imageApplicationService = imageApplicationService;
        this.categoryApplicationService = categoryApplicationService;
        this.cakeApplicationService = cakeApplicationService;
    }

    public Response<HabitHistory> createHabit(Long userId, Long categoryId) {
        // TODO: user exist validation
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User"));
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
                        cakeApplicationService.didReceiveFirstCake(userId, categoryId),
                        habit.getHabitRecords()
                )
        );
    }

    public Response<List<HabitHistory>> getHabit(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User"));
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
                                            cakeApplicationService.didReceiveFirstCake(userId, category.getId()),
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
        habits.remove(habit);
        habitService.refreshHabitsOrder(habits);
        habitRepository.deleteById(habitId);
        habitRepository.saveAll(habits);
        return new Response<>(habitId);
    }

    public HabitDoneResponse doneHabit(Long userId, Long habitId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User"));
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new ResourceNotFoundException("No Matched Habit"));
        if(!habit.isToday()) {
            throw new ResetContentsException("You can only done today's habit");
        }
        Category category = categoryApplicationService.findById(habit.getCategoryId());
        if (habit.isTodayDone()) {
            throw new AlreadyExistException("Already todayDone habit");
        }
        habit.todayDone();
        habit.refreshHabitHistory();

        return new HabitDoneResponse(
                new HabitCheckResponse(
                        habit.getId(),
                        category.getHabitType(),
                        cakeApplicationService.didReceiveFirstCake(userId, category.getId()),
                        habit.getHabitRecords(),
                        habit.getCategoryId()
                ),
                canReceiveNewCake(userId, habit) ? processGetNewCake(user, category) : null
        );
    }

    private NewCakeDTO processGetNewCake(User user, Category category) {
        SentPieceOfCake sentPieceOfCake = sentPieceOfCakeRepository.findRandomByUserIdAndCategoryId(user.getId(), category.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Has no remain cake message"));

        receivedPieceOfCakeRepository.save(
                ReceivedPieceOfCake.builder()
                        .userId(user.getId())
                        .categoryId(category.getId())
                        .sentPieceOfCakeId(sentPieceOfCake.getId())
                        .build()
        );

        if (isCollectedAsWholeCake(user.getId(), category.getId())) {
            wholeCakeRepository.save(new WholeCake(
                    user.getId(),
                    category.getId()
            ));
        }

        return new NewCakeDTO(
                user.getNickname(),
                sentPieceOfCake.getNote(),
                category.getCakeType().getName(),
                imageApplicationService.getMoveImagePath(category.getHabitType(), ImageEvent.NEW_CAKE),
                category.getCakeDescription().getMessage()
        );
    }

    private boolean isCollectedAsWholeCake(Long userId, Long categoryId) {
        int pieceCount = receivedPieceOfCakeRepository.countAllByUserIdAndCategoryId(userId, categoryId);
        if ((pieceCount + 1) % 8 == 0) {
            return true;
        }
        return false;
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

    private Habit findHabitById(List<Habit> habits, Long habitId) {
        for (Habit habit : habits) {
            if (habit.getId().equals(habitId)) {
                return habit;
            }
        }
        return null;
    }

    private Boolean canReceiveNewCake(Long userId, Habit habit) {
        Boolean isReadyToReceiveFirstCake = !cakeApplicationService.didReceiveFirstCake(userId, habit.getCategoryId());
        Boolean isReadyToReceiveNewCake = habit.isReadyToReceiveCake();
        return isReadyToReceiveFirstCake || isReadyToReceiveNewCake;
    }
}
