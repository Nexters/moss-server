package nexters.moss.server.web;

import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.web.value.ReceivedPieceOfCakeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class UserController {
    private UserApplicationService userApplicationService;
    private HttpServletRequest httpServletRequest;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response join(
            @RequestBody String nickname
    ) {
        return userApplicationService.join(
                (String) httpServletRequest.getAttribute("accessToken"),
                nickname
        );
    }

    @DeleteMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response leave() {
        return userApplicationService.leave((Long) httpServletRequest.getAttribute("userId"));
    }

    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<String> getUserInfo() {
        return userApplicationService.getUserInfo((Long) httpServletRequest.getAttribute("userId"));
    }

    @PostMapping("/report")
    @ResponseStatus(value = HttpStatus.OK)
    public Response report(
            @RequestBody ReceivedPieceOfCakeRequest receivedPieceOfCakeRequest
    ) {
        return userApplicationService.report(
                receivedPieceOfCakeRequest.getReceivedPieceOfCakeId(),
                receivedPieceOfCakeRequest.getReason()
        );
    }
}
