package nexters.moss.server.application;

import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
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

    public HabitApplicationService(
            CategoryRepository categoryRepository,
            HabitRepository habitRepository,
            HabitRecordRepository habitRecordRepository,
            HabitRecordService habitRecordService,
            HabitService habitService,
            UserRepository userRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.habitRepository = habitRepository;
        this.habitRecordRepository = habitRecordRepository;
        this.habitRecordService = habitRecordService;
        this.habitService = habitService;
        this.userRepository = userRepository;
    }

    public Response<HabitHistory> createHabit(Long userId, Long categoryId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("No Matched Category"));
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
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User"));

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

    public Response<HabitHistory> doneHabit(Long habitId) {
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new IllegalArgumentException("No Matched Habit"));
        habitService.doDoneHabit(habit);
        habitRepository.save(habit);

        List<HabitRecord> habitRecords = habit.getHabitRecords();
        if (habitRecordService.refreshHabitHistoryAndReturnIsChanged(habitRecords)) {
            habitRecords = habitRecordRepository.saveAll(habitRecords);
        }

        return new Response<>(
                new HabitHistory(
                        habit.getId(),
                        habit.getCategory().getHabitType(),
                        habit.getIsFirstCheck(),
                        habitRecords
                )
        );
    }
}
