package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.NewCakeDTO;
import nexters.moss.server.application.dto.cake.CreateNewCakeRequest;
import nexters.moss.server.config.exception.ResourceNotFoundException;
import nexters.moss.server.config.exception.UnauthorizedException;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.application.value.ImageEvent;
import nexters.moss.server.domain.value.CategoryType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CakeApplicationService {
    private UserRepository userRepository;
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;
    private ImageApplicationService imageApplicationService;
    private CategoryApplicationService categoryApplicationService;

    public CakeApplicationService(
            UserRepository userRepository,
            PieceOfCakeSendRepository pieceOfCakeSendRepository,
            PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository,
            ImageApplicationService imageApplicationService,
            CategoryApplicationService categoryApplicationService
    ) {
        this.userRepository = userRepository;
        this.pieceOfCakeSendRepository = pieceOfCakeSendRepository;
        this.pieceOfCakeReceiveRepository = pieceOfCakeReceiveRepository;
        this.imageApplicationService = imageApplicationService;
        this.categoryApplicationService = categoryApplicationService;
    }

    @Transactional
    public Response<Long> sendCake(Long userId, CreateNewCakeRequest createNewCakeRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User"));
        categoryApplicationService.findById(createNewCakeRequest.getCategoryId());
        return new Response<Long>(
                pieceOfCakeSendRepository.save(
                        SentPieceOfCake.builder()
                        .user(user)
                        .categoryId(createNewCakeRequest.getCategoryId())
                        .note(createNewCakeRequest.getNote())
                        .build()
                ).getId()
        );
    }

    public Response<NewCakeDTO> getCake(Long userId, Long categoryId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User"));
        CategoryType category = categoryApplicationService.findById(categoryId);
        SentPieceOfCake sentPieceOfCake = pieceOfCakeSendRepository.findRandomByUser_IdAndCategoryId(userId, categoryId).orElseThrow(() -> new ResourceNotFoundException("Has no remain cake message"));

        ReceivedPieceOfCake receivedPOC = pieceOfCakeReceiveRepository.save(
                ReceivedPieceOfCake.builder()
                        .user(user)
                        .categoryId(categoryId)
                        .sentPieceOfCake(sentPieceOfCake)
                        .build()
        );

        return new Response<NewCakeDTO>(
                new NewCakeDTO(
                        receivedPOC.getUser().getNickname(),
                        receivedPOC.getSentPieceOfCake().getNote(),
                        category.getCakeType().getName(),
                        imageApplicationService.getMoveImagePath(category.getHabitType(), ImageEvent.NEW_CAKE))
        );
    }
}
