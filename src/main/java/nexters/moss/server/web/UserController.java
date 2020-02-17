package nexters.moss.server.web;

import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.web.value.ReceivedPieceOfCakeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    private UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response join(
            @RequestHeader String accessToken,
            @RequestBody String nickname
    ) {
        return userApplicationService.join(accessToken, nickname);
    }

    @DeleteMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response leave(
            @RequestHeader String habikeryToken
    ) {
        return userApplicationService.leave(habikeryToken);
    }

    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<String> getUserInfo(
            @RequestHeader String habikeryToken
    ) {
        return userApplicationService.getUserInfo(habikeryToken);
    }

    @PostMapping("/report")
    @ResponseStatus(value = HttpStatus.OK)
    public Response report(
            @RequestHeader String habikeryToken,
            @RequestBody ReceivedPieceOfCakeRequest receivedPieceOfCakeRequest
    ) {
        return userApplicationService.report(
                habikeryToken,
                receivedPieceOfCakeRequest.getReceivedPieceOfCakeId(),
                receivedPieceOfCakeRequest.getReason()
        );
    }
}
