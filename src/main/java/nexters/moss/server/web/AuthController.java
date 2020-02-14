package nexters.moss.server.web;

import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.UserLogin;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    private UserApplicationService userApplicationService;

    public AuthController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping("login")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<UserLogin> login(
            @RequestHeader String accessToken
    ) {
        return userApplicationService.login(accessToken);
    }
}
