package nexters.moss.server.application;

import lombok.AllArgsConstructor;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.config.exception.UserInfoException;
import nexters.moss.server.domain.model.Token;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.PieceOfCakeReceiveRepository;
import nexters.moss.server.domain.repository.ReportRepository;
import nexters.moss.server.domain.service.SocialTokenService;
import nexters.moss.server.domain.service.HabikeryTokenService;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.UserRepository;
import org.springframework.dao.DuplicateKeyException;
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
            throw new DuplicateKeyException("Duplicated Social ID User");
        }

        User newUser = userRepository.save(
                User.builder()
                        .socialId(socialId)
                        .nickname(nickname)
                        .build()
        );

        return new Response();
    }

    public Response<String> login(String accessToken) {
        Long socialId = socialTokenService.getSocialUserId(accessToken);
        User user = userRepository.findBySocialId(socialId).orElseThrow(() -> new UserInfoException("No Matched Habikery User with Social ID"));

        String habikeryToken = habikeryTokenService.createToken(user.getId(), accessToken);
        User updatedUser = userRepository.save(
                new User(
                        user.getId(),
                        user.getSocialId(),
                        habikeryToken,
                        user.getNickname(),
                        user.getHabits()
                )
        );

        return new Response<>(updatedUser.getHabikeryToken());
    }

    public Response leave(String habikeryToken) {
        Token token = habikeryTokenService.recoverToken(habikeryToken);
        User user = userRepository.findById(token.getUserId()).orElseThrow(() -> new UserInfoException("No Matched Habikery User with User ID"));

        userRepository.deleteById(user.getId());

        return new Response();
    }

    public Response<String> getUserInfo(String habikeryToken) {
        Token token = habikeryTokenService.recoverToken(habikeryToken);
        User user = userRepository.findById(token.getUserId()).orElseThrow(() -> new UserInfoException("No Matched Habikery User with User ID"));

        return new Response<>(user.getNickname());
    }

    @Transactional
    public Response report(String habikeryToken, Long receivedPieceOfCakeId, String reason) {
        Token token = habikeryTokenService.recoverToken(habikeryToken);

        ReceivedPieceOfCake receivedPieceOfCake = pieceOfCakeReceiveRepository.findById(receivedPieceOfCakeId).orElseThrow(() -> new IllegalArgumentException("No Matched ReceivedPieceOfCake"));
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
