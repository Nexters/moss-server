package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.ReceivedPieceOfCake;
import nexters.moss.server.domain.model.SentPieceOfCake;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.HabitRepository;
import nexters.moss.server.domain.repository.PieceOfCakeReceiveRepository;
import nexters.moss.server.domain.repository.PieceOfCakeSendRepository;
import nexters.moss.server.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class CakeApplicationService {
    private HabitRepository habitRepository;
    private UserRepository userRepository;
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;

    public CakeApplicationService(HabitRepository habitRepository, UserRepository userRepository, PieceOfCakeSendRepository pieceOfCakeSendRepository, PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
        this.pieceOfCakeSendRepository = pieceOfCakeSendRepository;
        this.pieceOfCakeReceiveRepository = pieceOfCakeReceiveRepository;
    }

    public Response<List<Habit>> getAllHabits() {
        List<Habit> habits = habitRepository.findAll();
        return new Response<List<Habit>>(habits);
    }

    @Transactional
    public Response<SentPieceOfCake> addSentPieceOfCake(long userId, Map<String, Object> data){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User Id"));

        Long categoryId = (Long) data.get("categoryId");
        Habit habit = habitRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("No Matched Habit Id"));

        String message = (String) data.get("message");

        SentPieceOfCake sentPieceOfCake = pieceOfCakeSendRepository.save(SentPieceOfCake.builder().user(user).habit(habit).note(message).build());
        return new Response<SentPieceOfCake>(sentPieceOfCake);
    }


    @Transactional
    public  Response<ReceivedPieceOfCake> addReceivedPieceOfCake(long userId, long categoryId){
        SentPieceOfCake sentPieceOfCake = pieceOfCakeSendRepository.findRandomByHabit(categoryId);

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User Id"));
        ReceivedPieceOfCake receivedPieceOfCake = pieceOfCakeReceiveRepository.save(ReceivedPieceOfCake.builder().sentPieceOfCake(sentPieceOfCake).user(user).build());

        return new Response<ReceivedPieceOfCake>(receivedPieceOfCake);
    }
}
