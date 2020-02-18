package nexters.moss.server.web;

import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.infra.auth.Http;
import nexters.moss.server.web.value.ReceivedPieceOfCakeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
            HttpServletRequest request,
            @RequestBody String nickname
    ) {
        return userApplicationService.join(
                (String)request.getAttribute("accessToken"),
                nickname
        );
    }

    @DeleteMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response leave(
            HttpServletRequest request
    ) {
        return userApplicationService.leave((Long)request.getAttribute("userId"));
    }

    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<String> getUserInfo(
            HttpServletRequest request
    ) {
        return userApplicationService.getUserInfo((Long)request.getAttribute("userId"));
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
