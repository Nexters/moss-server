package nexters.moss.server.application;

import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.NewCakeDTO;
import nexters.moss.server.application.dto.cake.CreateNewCakeRequest;
import nexters.moss.server.domain.exceptions.ResourceNotFoundException;
import nexters.moss.server.domain.exceptions.UnauthorizedException;
import nexters.moss.server.domain.Category;
import nexters.moss.server.domain.cake.ReceivedPieceOfCake;
import nexters.moss.server.domain.cake.ReceivedPieceOfCakeRepository;
import nexters.moss.server.domain.cake.SentPieceOfCake;
import nexters.moss.server.domain.cake.SentPieceOfCakeRepository;
import nexters.moss.server.application.value.ImageEvent;
import nexters.moss.server.domain.user.User;
import nexters.moss.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CakeApplicationService {
    private UserRepository userRepository;
    private SentPieceOfCakeRepository sentPieceOfCakeRepository;
    private ReceivedPieceOfCakeRepository receivedPieceOfCakeRepository;
    private ImageApplicationService imageApplicationService;
    private CategoryApplicationService categoryApplicationService;

    public CakeApplicationService(
            UserRepository userRepository,
            SentPieceOfCakeRepository sentPieceOfCakeRepository,
            ReceivedPieceOfCakeRepository receivedPieceOfCakeRepository,
            ImageApplicationService imageApplicationService,
            CategoryApplicationService categoryApplicationService
    ) {
        this.userRepository = userRepository;
        this.sentPieceOfCakeRepository = sentPieceOfCakeRepository;
        this.receivedPieceOfCakeRepository = receivedPieceOfCakeRepository;
        this.imageApplicationService = imageApplicationService;
        this.categoryApplicationService = categoryApplicationService;
    }

    @Transactional
    public Response<Long> sendCake(Long userId, CreateNewCakeRequest createNewCakeRequest) {
        // TODO: user exist validation
        categoryApplicationService.findById(createNewCakeRequest.getCategoryId());
        return new Response<Long>(
                sentPieceOfCakeRepository.save(
                        SentPieceOfCake.builder()
                        .userId(userId)
                        .categoryId(createNewCakeRequest.getCategoryId())
                        .note(createNewCakeRequest.getNote())
                        .build()
                ).getId()
        );
    }

    public Response<NewCakeDTO> getCake(Long userId, Long categoryId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("No Matched User"));
        Category category = categoryApplicationService.findById(categoryId);
        SentPieceOfCake sentPieceOfCake = sentPieceOfCakeRepository.findRandomByUserIdAndCategoryId(userId, categoryId).orElseThrow(() -> new ResourceNotFoundException("Has no remain cake message"));

        ReceivedPieceOfCake receivedPOC = receivedPieceOfCakeRepository.save(
                ReceivedPieceOfCake.builder()
                        .userId(userId)
                        .categoryId(categoryId)
                        .sentPieceOfCakeId(sentPieceOfCake.getId())
                        .build()
        );

        return new Response<NewCakeDTO>(
                new NewCakeDTO(
                        user.getNickname(),
                        sentPieceOfCake.getNote(),
                        category.getCakeType().getName(),
                        imageApplicationService.getMoveImagePath(category.getHabitType(), ImageEvent.NEW_CAKE))
        );
    }
}
