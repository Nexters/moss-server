package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.NewCakeDTO;
import nexters.moss.server.application.dto.cake.CreateNewCakeRequest;
import nexters.moss.server.config.exception.ResourceNotFoundException;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.domain.service.ImageService;
import nexters.moss.server.domain.value.ImageEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CakeApplicationService {
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;
    private ImageService imageService;

    public CakeApplicationService(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            PieceOfCakeSendRepository pieceOfCakeSendRepository,
            PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository,
            ImageService imageService) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.pieceOfCakeSendRepository = pieceOfCakeSendRepository;
        this.pieceOfCakeReceiveRepository = pieceOfCakeReceiveRepository;
        this.imageService = imageService;
    }

    @Transactional
    public Response<Long> createNewCake(CreateNewCakeRequest createNewCakeRequest) {
        User user = userRepository.findById(createNewCakeRequest.getUserId()).orElseThrow(() -> new IllegalArgumentException("No Matched User"));
        Category category = categoryRepository.findById(createNewCakeRequest.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("No Matched Category"));
        return new Response<Long>(
                pieceOfCakeSendRepository.save(
                        SentPieceOfCake.builder()
                        .user(user)
                        .category(category)
                        .note(createNewCakeRequest.getNote())
                        .build()
                ).getId()
        );
    }

    @Transactional
    public Response<NewCakeDTO> getNewCake(Long userId, Long categoryId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("No Matched User"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("No Matched Category"));
        SentPieceOfCake sentPieceOfCake = pieceOfCakeSendRepository.findRandomByUser_IdAndCategory_Id(userId, categoryId).orElseThrow(() -> new ResourceNotFoundException("Has no remain cake message"));

        ReceivedPieceOfCake receivedPOC = pieceOfCakeReceiveRepository.save(
                ReceivedPieceOfCake.builder()
                        .user(user)
                        .category(category)
                        .sentPieceOfCake(sentPieceOfCake)
                        .build()
        );

        return new Response<NewCakeDTO>(
                new NewCakeDTO(
                        receivedPOC.getUser().getNickname(),
                        receivedPOC.getSentPieceOfCake().getNote(),
                        receivedPOC.getSentPieceOfCake().getCategory().getCakeType().getName(),
                        imageService.getMoveImagePath(category.getHabitType(), ImageEvent.NEW_CAKE))
        );
    }


}
