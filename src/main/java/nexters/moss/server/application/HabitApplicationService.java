package nexters.moss.server.application;

import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.HabitRecordRepository;
import nexters.moss.server.domain.repository.HabitRepository;
import nexters.moss.server.domain.repository.UserRepository;
import nexters.moss.server.domain.service.HabitRecordService;
import nexters.moss.server.domain.value.HabitStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class HabitApplicationService {
    private HabitRepository habitRepository;
    private HabitRecordRepository habitRecordRepository;
    private HabitRecordService habitRecordService;
    private UserRepository userRepository;

    public HabitApplicationService(
            HabitRepository habitRepository,
            HabitRecordRepository habitRecordRepository,
            HabitRecordService habitRecordService,
            UserRepository userRepository
    ) {
        this.habitRepository = habitRepository;
        this.habitRecordRepository = habitRecordRepository;
        this.habitRecordService = habitRecordService;
        this.userRepository = userRepository;
    }

    public Response<HabitHistory> createHabit(Long userId, Long habitId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User"));
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new IllegalArgumentException("No Matched HabitType"));

        List<HabitRecord> habitRecords = habitRecordService.createHabitRecords(user, habit);
        habitRecords = habitRecordRepository.saveAll(habitRecords);
        return new Response(
                new HabitHistory(habit.getId(), habit.getHabitType(), habitRecords)
        );
    }

    public Response<List<HabitHistory>> getHabitHistory(Long userId) {
        List<HabitHistory> habitHistories = new ArrayList<>();
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User"));

        Map<Habit, List<HabitRecord>> habitHistoryMap = user.getHabitRecords()
                .stream()
                .collect(Collectors.groupingBy(HabitRecord::getHabit));


        habitHistoryMap.forEach((habit, habitRecords) -> {
            habitHistories.add(
                    new HabitHistory(
                            habit.getId(),
                            habit.getHabitType(),
                            habitRecordService.validateAndRefreshHabitHistory(habitRecords)
                    )
            );
        });

        return new Response<>(habitHistories);
    }

    public Response<Long> deleteHabit(Long userId, Long habitId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User"));
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new IllegalArgumentException("No Matched HabitType"));
        habitRecordRepository.deleteAllByUserAndHabit(user, habit);
        return new Response<>(habitId);
    }
}
