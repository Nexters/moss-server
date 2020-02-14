package nexters.moss.server.web;

import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("token")
public class TokenController {
    private UserApplicationService userApplicationService;

    public TokenController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<String> login(
            @RequestHeader String accessToken
    ) {
        return userApplicationService.login(accessToken);
    }
}
