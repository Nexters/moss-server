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
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;

    public CakeApplicationService(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            PieceOfCakeSendRepository pieceOfCakeSendRepository,
            PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository
    ) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.pieceOfCakeSendRepository = pieceOfCakeSendRepository;
        this.pieceOfCakeReceiveRepository = pieceOfCakeReceiveRepository;
    }

    @Transactional
    public Response<Long> createNewCake(CreateNewCakeRequest createNewCakeRequest) {
        return new Response<Long>(
                pieceOfCakeSendRepository.save(
                        new SentPieceOfCake(createNewCakeRequest))
                        .getId()
        );
    }

    @Transactional
    public Response<NewCakeDTO> getNewCake(Long userId, Long categoryId) {
        User user = userRepository.findById(userId).get();
        Category category = categoryRepository.findById(categoryId).get();
        ReceivedPieceOfCake receivedPOC = pieceOfCakeReceiveRepository.save(
                ReceivedPieceOfCake.builder()
                .user(user)
                .category(category)
                .sentPieceOfCake(pieceOfCakeSendRepository.findRandomByUser_IdAndCategory_Id(userId, categoryId))
                .build()
        );

        return new Response<NewCakeDTO>(
                new NewCakeDTO(
                        receivedPOC.getUser().getNickname(),
                        receivedPOC.getSentPieceOfCake().getNote(),
                        receivedPOC.getSentPieceOfCake().getCategory().getCakeType().getName())
        );
    }


}
