package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.NewCakeDTO;
import nexters.moss.server.application.dto.cake.CreateANewCakeRequest;
import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.ReceivedPieceOfCake;
import nexters.moss.server.domain.model.SentPieceOfCake;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.HabitRepository;
import nexters.moss.server.domain.repository.PieceOfCakeReceiveRepository;
import nexters.moss.server.domain.repository.PieceOfCakeSendRepository;
import nexters.moss.server.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CakeApplicationService {
    private HabitRepository habitRepository;
    private UserRepository userRepository;
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;

    public CakeApplicationService(HabitRepository habitRepository, UserRepository userRepository,
                                  PieceOfCakeSendRepository pieceOfCakeSendRepository,
                                  PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
        this.pieceOfCakeSendRepository = pieceOfCakeSendRepository;
        this.pieceOfCakeReceiveRepository = pieceOfCakeReceiveRepository;
    }

    @Transactional
    public Response<Long> createANewCake(CreateANewCakeRequest createANewCakeRequest) {
        User user =
                userRepository.findById(createANewCakeRequest.getUserId()).orElseThrow(() -> new IllegalArgumentException("No Matched User Id"));
        Habit habit =
                habitRepository.findById(createANewCakeRequest.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("No Matched Habit Id"));

        return new Response<Long>(
                pieceOfCakeSendRepository.save(
                        SentPieceOfCake
                                .builder()
                                .user(user)
                                .habit(habit)
                                .note(createANewCakeRequest.getNote())
                                .build())
                        .getId()
        );
    }


    @Transactional
    public Response<NewCakeDTO> getANewCake(Long userId, Long categoryId) {
        SentPieceOfCake sentPieceOfCake = pieceOfCakeSendRepository.findRandomOneByHabit(userId, categoryId);

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User " +
                "Id"));

        ReceivedPieceOfCake receivedPOC =
                pieceOfCakeReceiveRepository.save(ReceivedPieceOfCake.builder().sentPieceOfCake(sentPieceOfCake).user(user).build());

        return new Response<NewCakeDTO>(new NewCakeDTO(receivedPOC.getUser().getNickname()
                                            ,receivedPOC.getSentPieceOfCake().getNote()
                                            ,receivedPOC.getSentPieceOfCake().getHabit().getCakeType().getName()));
    }


}
