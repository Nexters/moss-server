package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.NewCakeDTO;
import nexters.moss.server.application.dto.cake.CreateNewCakeRequest;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CakeApplicationService {
    private HabitRepository habitRepository;
    private UserRepository userRepository;
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;
    private CategoryRepository categoryRepository;

    public CakeApplicationService(HabitRepository habitRepository,
                                  UserRepository userRepository, PieceOfCakeSendRepository pieceOfCakeSendRepository,
                                  PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository,
                                  CategoryRepository categoryRepository) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
        this.pieceOfCakeSendRepository = pieceOfCakeSendRepository;
        this.pieceOfCakeReceiveRepository = pieceOfCakeReceiveRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Response<Long> createANewCake(CreateNewCakeRequest createNewCakeRequest) {
        User user =
                userRepository.findById(createNewCakeRequest.getUserId()).orElseThrow(() -> new IllegalArgumentException("No Matched User Id"));
        Habit habit =
                habitRepository.findById(createNewCakeRequest.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("No Matched Habit Id"));

        return new Response<Long>(
                pieceOfCakeSendRepository.save(
                        SentPieceOfCake
                                .builder()
                                .user(user)
                                .habit(habit)
                                .category(habit.getCategory())
                                .note(createNewCakeRequest.getNote())
                                .build())
                                .getId()
        );
    }


    @Transactional
    public Response<NewCakeDTO> getANewCake(Long userId, Long categoryId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User Id"));
        Category category = categoryRepository.findById(categoryId).get();
        SentPieceOfCake sentPieceOfCake = pieceOfCakeSendRepository.findRandomOneByHabit(userId, categoryId);

        ReceivedPieceOfCake receivedPOC =
                pieceOfCakeReceiveRepository.save(ReceivedPieceOfCake.builder().user(user).sentPieceOfCake(sentPieceOfCake).category(category).build());

        return new Response<NewCakeDTO>(new NewCakeDTO(receivedPOC.getUser().getNickname()
                                            ,receivedPOC.getSentPieceOfCake().getNote()
                                            ,receivedPOC.getSentPieceOfCake().getCategory().getCakeType().getName()));
    }


}
