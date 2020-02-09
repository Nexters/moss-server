package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.HabitRepository;
import nexters.moss.server.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CakeApplicationService {
    private final HabitRepository habitRepository;
    private UserRepository userRepository;

    public CakeApplicationService(HabitRepository habitRepository, UserRepository userRepository) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
    }

    public Response<List<Habit>> getAllHabits() {
        List<Habit> habits = habitRepository.findAll();
        return new Response<List<Habit>>(habits);
    }

    public boolean addCake(long userId, Map<String, Object> data){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User Id"));
        return true;
    }
}
