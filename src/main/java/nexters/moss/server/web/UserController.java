package nexters.moss.server.web;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.web.value.ReceivedPieceOfCakeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private UserApplicationService userApplicationService;
    private HttpServletRequest httpServletRequest;

    @ApiOperation(value = "회원 가입",
            notes = "소셜 로그인을 사용하여 회원 가입을 진행할 경우 사용합니다. \n"
                    + "클라이언트는 카카오 서버로부터 `accessToken`을 발급 받습니다. \n"
                    + "발급 받은 `accessToken`와 사용자가 사용할 `nickname`을 서버에 전달하면, 서버는 해당 토큰 검증과 가입을 진행합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "회원 가입 하려는 회원의 카카오 access token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "habikeryToken", value = "여기서는 사용하지 않습니다.", required = false, dataType = "String", paramType = "header")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적인 응답"),
            @ApiResponse(code = 401, message = "토큰 관련 오류"),
            @ApiResponse(code = 404, message = "찾을 수 없는 리소스 요청"),
            @ApiResponse(code = 409, message = "중복된 리소스 오류")
    })
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Response join(
            @ApiParam(value = "신규 회원이 입력한 닉네임", required = true) @RequestBody String nickname
    ) {
        return userApplicationService.join(
                (String) httpServletRequest.getAttribute("accessToken"),
                nickname
        );
    }

    @ApiOperation(value = "회원 탈퇴",
            notes = "회원을 탈퇴할 경우, 클라이언트는 회원의 habikeryToken을 서버에 전달합니다. \n"
            + "이후, 받은 회원 정보에 맞는 회원 데이터를 삭제하여 탈퇴 절차를 진행합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "여기서는 사용하지 않습니다.", required = false, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "habikeryToken", value = "탈퇴하려는 회원의 habikery token", required = true, dataType = "String", paramType = "header")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적인 응답"),
            @ApiResponse(code = 401, message = "토큰 관련 오류"),
            @ApiResponse(code = 404, message = "찾을 수 없는 리소스 요청"),
            @ApiResponse(code = 409, message = "중복된 리소스 오류")
    })
    @DeleteMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Response<Long> leave() {
        return userApplicationService.leave((Long) httpServletRequest.getAttribute("userId"));
    }

    @ApiOperation(value = "내 회원 정보 조회",
            notes = "조회하려는 회원의 habikeryToken을 서버에 전달하면, 해당 회원 정보(userId, nickname)를 돌려줍니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "여기서는 사용하지 않습니다.", required = false, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "habikeryToken", value = "조회하려는 회원의 habikery token", required = true, dataType = "String", paramType = "header")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적인 응답"),
            @ApiResponse(code = 401, message = "토큰 관련 오류"),
            @ApiResponse(code = 404, message = "찾을 수 없는 리소스 요청"),
            @ApiResponse(code = 409, message = "중복된 리소스 오류")
    })
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Response<String> getUserInfo() {
        return userApplicationService.getUserInfo((Long) httpServletRequest.getAttribute("userId"));
    }

    @ApiOperation(value = "회원 신고",
            notes = "신고하려는 케이크 메시지를 서버에 전달합니다. \n"
            + "서버는 해당 케이크 메시지를 작성한 사용자에 신고 처리합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "여기서는 사용하지 않습니다.", required = false, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "habikeryToken", value = "신고를 요청하는 회원의 habikery token", required = true, dataType = "String", paramType = "header")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적인 응답"),
            @ApiResponse(code = 401, message = "토큰 관련 오류"),
            @ApiResponse(code = 404, message = "찾을 수 없는 리소스 요청"),
            @ApiResponse(code = 409, message = "중복된 리소스 오류")
    })
    @PostMapping("/report")
    @ResponseStatus(value = HttpStatus.OK)
    public Response report(
            @ApiParam(value = "신고하고자 하는 케이크 메시지 정보", required = true) @RequestBody ReceivedPieceOfCakeRequest receivedPieceOfCakeRequest
    ) {
        return userApplicationService.report(
                receivedPieceOfCakeRequest.getReceivedPieceOfCakeId(),
                receivedPieceOfCakeRequest.getReason()
        );
    }
}
