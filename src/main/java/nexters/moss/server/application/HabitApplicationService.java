package nexters.moss.server.application;

import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.Category;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.CategoryRepository;
import nexters.moss.server.domain.repository.HabitRecordRepository;
import nexters.moss.server.domain.repository.HabitRepository;
import nexters.moss.server.domain.repository.UserRepository;
import nexters.moss.server.domain.service.HabitRecordService;
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
    private UserRepository userRepository;

    public HabitApplicationService(
            CategoryRepository categoryRepository,
            HabitRepository habitRepository,
            HabitRecordRepository habitRecordRepository,
            HabitRecordService habitRecordService,
            UserRepository userRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.habitRepository = habitRepository;
        this.habitRecordRepository = habitRecordRepository;
        this.habitRecordService = habitRecordService;
        this.userRepository = userRepository;
    }

    public Response<HabitHistory> createHabit(Long userId, Long categoryId) {
        Habit habit = habitRepository.save(new Habit(categoryId, userId));
        habit.onActivation();
        return new Response(
                new HabitHistory(
                        habit.getId(),
                        habit.getCategory().getHabitType(),
                        habit.getIsFirstCheck(),
                        habitRecordRepository.saveAll(
                                habitRecordService.createHabitRecords(userId, habit.getId())
                        )
                )
        );
    }

    public Response<List<HabitHistory>> getHabitHistory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User"));
        return new Response<>(
                user.getHabits()
                        .stream()
                        .map(habit -> new HabitHistory(
                                        habit.getId(),
                                        habit.getCategory().getHabitType(),
                                        habit.getIsFirstCheck(),
                                        // TODO: refresh 되었다면 habitRecord db에 반영
                                        habitRecordService.validateAndRefreshHabitHistory(habit.getHabitRecords())
                                )
                        )
                        .collect(Collectors.toList())
        );
    }

    public Response<Long> deleteHabit(Long userId, Long habitId) {
        habitRecordRepository.deleteAllByUser_IdAndHabit_Id(userId, habitId);
        return new Response<>(habitId);
    }

    public Response<HabitHistory> doneHabit(Long habitId) {
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new IllegalArgumentException("No Matched Habit"));
        habit.onFirstCheck();

        habitRepository.save(habit);
        return new Response<>(
                new HabitHistory(
                        habit.getId(),
                        habit.getCategory().getHabitType(),
                        habit.getIsFirstCheck(),
                        // TODO: refresh 되었다면 habitRecord db에 반영
                        habitRecordService.validateAndRefreshHabitHistory(habit.getHabitRecords())
                )
        );
    }
}
