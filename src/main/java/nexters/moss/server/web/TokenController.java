package nexters.moss.server.web;

import io.swagger.annotations.*;
import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("token")
public class TokenController {
    private UserApplicationService userApplicationService;

    public TokenController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @ApiOperation(value = "회원 로그인",
            notes = "서비스에 가입된 회원이 로그인할 때 사용합니다.\n"
                    + "클라이언트가 로그인 하고자 하는 회원의 `accessToken`을 서버에 전달해야 합니다.\n"
                    + "서버는 해당 회원에 대해 `habikeryToken`을 발행하여 반환합니다.\n"
                    + "클라이언트는 로그인 이후, API를 사용하려면 `habikeryToken`을 사용해야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적인 응답"),
            @ApiResponse(code = 401, message = "토큰 관련 오류"),
            @ApiResponse(code = 404, message = "찾을 수 없는 리소스 요청"),
            @ApiResponse(code = 409, message = "중복된 리소스 오류")
    })
    @PostMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<String> login(
            @ApiParam(value = "로그인 하려는 회원의 카카오 access token", required = true) @RequestHeader String accessToken
    ) {
        return userApplicationService.login(accessToken);
    }
}
