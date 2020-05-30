package nexters.moss.server.application;

import lombok.AllArgsConstructor;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.cake.*;
import nexters.moss.server.domain.exceptions.AlreadyExistException;
import nexters.moss.server.domain.exceptions.ResourceNotFoundException;
import nexters.moss.server.domain.exceptions.UnauthorizedException;
import nexters.moss.server.domain.habit.HabitRecordRepository;
import nexters.moss.server.domain.habit.HabitRepository;
import nexters.moss.server.domain.user.SocialTokenService;
import nexters.moss.server.domain.user.HabikeryTokenService;
import nexters.moss.server.domain.user.User;
import nexters.moss.server.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserApplicationService {
    private SocialTokenService socialTokenService;
    private HabikeryTokenService habikeryTokenService;
    private UserRepository userRepository;
    private ReceivedPieceOfCakeRepository receivedPieceOfCakeRepository;
    private SentPieceOfCakeRepository sentPieceOfCakeRepository;
    private WholeCakeRepository wholeCakeRepository;
    private HabitRecordRepository habitRecordRepository;
    private HabitRepository habitRepository;

    public Response join(String accessToken, String nickname) {
        Long socialId = socialTokenService.getSocialUserId(accessToken);

        if (userRepository.existsBySocialId(socialId)) {
            throw new AlreadyExistException("Duplicated Habikery User with Social ID: " + socialId.toString());
        }

        String trimmedNickname = trimDoubleQuotes(nickname);

        userRepository.save(
                User.builder()
                        .socialId(socialId)
                        .nickname(trimmedNickname)
                        .build()
        );

        return new Response();
    }

    public Response<String> login(String accessToken) {
        Long socialId = socialTokenService.getSocialUserId(accessToken);
        User user = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new UnauthorizedException("No Matched Habikery User with Social ID: " + socialId.toString()));

        String habikeryToken = habikeryTokenService.createToken(user.getId(), accessToken);
        user.setHabikeryToken(habikeryToken);
        return new Response<>(habikeryToken);
    }

    public Response<Long> leave(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("No Matched Habikery User with User ID");
        }

        userRepository.deleteById(userId);
        receivedPieceOfCakeRepository.deleteByUserId(userId);
        wholeCakeRepository.deleteByUserId(userId);
        habitRecordRepository.deleteByUserId(userId);
        habitRepository.deleteByUserId(userId);

        return new Response(userId);
    }

    public Response<String> getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No Matched Habikery User with User ID"));

        return new Response<>(user.getNickname());
    }

    public Response report(Long receivedPieceOfCakeId, String reason) {
        ReceivedPieceOfCake receivedPieceOfCake = receivedPieceOfCakeRepository.findById(receivedPieceOfCakeId)
                .orElseThrow(() -> new ResourceNotFoundException("No Matched ReceivedPieceOfCake"));
        SentPieceOfCake sentPieceOfCake = sentPieceOfCakeRepository.findById(receivedPieceOfCake.getSentPieceOfCakeId())
                .orElseThrow(() -> new ResourceNotFoundException("No Matched SentPieceOfCake"));
        User reportedUser = userRepository.findById(
                sentPieceOfCake.getUserId()
        ).orElseThrow(() -> new ResourceNotFoundException("No Matched Habikery User with User ID"));

        reportedUser.reported(reason);
        return new Response();
    }

    private String trimDoubleQuotes(String nickname) {
        String trimmedNickname = nickname;

        if (nickname.startsWith("\"")) {
            trimmedNickname = trimmedNickname.substring(1);
        }

        if (nickname.endsWith("\"")) {
            trimmedNickname = trimmedNickname.substring(0, trimmedNickname.length() - 1);
        }

        return trimmedNickname;
    }
}
