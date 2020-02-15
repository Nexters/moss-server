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
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;

    public CakeApplicationService(
            PieceOfCakeSendRepository pieceOfCakeSendRepository,
            PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository
    ) {
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
        ReceivedPieceOfCake receivedPOC = pieceOfCakeReceiveRepository.save(
                new ReceivedPieceOfCake(
                        userId,
                        pieceOfCakeSendRepository.findRandomByUser_IdAndHabit_Id(userId, categoryId).getId(),
                        categoryId
                )
        );

        return new Response<NewCakeDTO>(
                new NewCakeDTO(
                        receivedPOC.getUser().getNickname(),
                        receivedPOC.getSentPieceOfCake().getNote(),
                        receivedPOC.getSentPieceOfCake().getCategory().getCakeType().getName())
        );
    }


}
