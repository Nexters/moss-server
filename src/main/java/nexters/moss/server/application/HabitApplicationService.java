package nexters.moss.server.application;

import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.HabitRepository;
import nexters.moss.server.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HabitApplicationService {
    private HabitRepository habitRepository;
    private UserRepository userRepository;

    public HabitApplicationService(
            HabitRepository habitRepository,
            UserRepository userRepository
    ) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
    }

    public Response<List<HabitHistory>> getHabitHistory(Long userId) {
        List<HabitHistory> habitHistories = new ArrayList<>();
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User Id"));
        Map<Habit, List<HabitRecord>> habitHistoryMap = user.getHabitRecords()
                .stream()
                .collect(Collectors.groupingBy(HabitRecord::getHabit));
        habitHistoryMap.forEach((habit, habitRecords) -> {
            habitHistories.add(
                    new HabitHistory(habit.getId(), habit.getHabitName(), habitRecords)
            );
        });

        return new Response<>(habitHistories);
    }
}
