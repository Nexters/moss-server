package nexters.moss.server.web;

import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
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
            @RequestHeader String accountToken
    ) {
        return userApplicationService.leave(accountToken);
    }

    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<String> getUserInfo(
            @RequestHeader String accountToken
    ) {
        return userApplicationService.getUserInfo(accountToken);
    }

    @PostMapping("/report")
    @ResponseStatus(value = HttpStatus.OK)
    public Response report(
            @RequestHeader String accountToken,
            @RequestBody Long receivedPieceOfCakeId,
            @RequestBody String reason
    ) {
        return userApplicationService.report(accountToken, receivedPieceOfCakeId, reason);
    }
}
