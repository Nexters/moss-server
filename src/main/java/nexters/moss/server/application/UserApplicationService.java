package nexters.moss.server.application;

import lombok.AllArgsConstructor;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.config.exception.HabikeryUserDuplicatedException;
import nexters.moss.server.config.exception.HabikeryUserNotFoundException;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.domain.service.SocialTokenService;
import nexters.moss.server.domain.service.HabikeryTokenService;
import nexters.moss.server.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserApplicationService {
    private SocialTokenService socialTokenService;
    private HabikeryTokenService habikeryTokenService;
    private UserRepository userRepository;
    private ReportRepository reportRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;

    public Response join(String accessToken, String nickname) {
        Long socialId = socialTokenService.getSocialUserId(accessToken);

        if (userRepository.existsBySocialId(socialId)) {
            throw new HabikeryUserDuplicatedException("Duplicated Habikery User with Social ID");
        }

        userRepository.save(
                User.builder()
                        .socialId(socialId)
                        .nickname(nickname)
                        .build()
        );

        return new Response();
    }

    public Response<String> login(String accessToken) {
        Long socialId = socialTokenService.getSocialUserId(accessToken);
        User user = userRepository.findBySocialId(socialId).orElseThrow(() -> new HabikeryUserNotFoundException("No Matched Habikery User with Social ID"));

        String habikeryToken = habikeryTokenService.createToken(user.getId(), accessToken);
        user.setHabikeryToken(habikeryToken);
        User updatedUser = userRepository.save(user);

        return new Response<>(updatedUser.getHabikeryToken());
    }

    public Response<Long> leave(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw  new HabikeryUserNotFoundException("No Matched Habikery User with User ID");
        }
        userRepository.deleteById(userId);
        return new Response(userId);
    }

    public Response<String> getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new HabikeryUserNotFoundException("No Matched Habikery User with User ID"));

        return new Response<>(user.getNickname());
    }

    public Response report(Long receivedPieceOfCakeId, String reason) {
        ReceivedPieceOfCake receivedPieceOfCake = pieceOfCakeReceiveRepository.findById(receivedPieceOfCakeId).orElseThrow(() -> new HabikeryUserNotFoundException("No Matched ReceivedPieceOfCake"));
        User reportedUser = receivedPieceOfCake.getSentPieceOfCake().getUser();

        reportRepository.save(
                Report.builder()
                .user(reportedUser)
                .reason(reason)
                .build()
        );

        return new Response();
    }
}
