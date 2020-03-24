package nexters.moss.server.web;

import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.dto.cake.*;
import nexters.moss.server.application.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cake")
public class CakeController {
    private CakeApplicationService cakeApplicationService;
    private HttpServletRequest httpServletRequest;

    public CakeController(
            CakeApplicationService cakeApplicationService,
            HttpServletRequest httpServletRequest
    ) {
        this.cakeApplicationService = cakeApplicationService;
        this.httpServletRequest = httpServletRequest;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Response<Long> sendCake(@RequestBody CreateNewCakeRequest createNewCakeRequest) {
        return cakeApplicationService.sendCake(
                (Long)httpServletRequest.getAttribute("userId"),
                createNewCakeRequest
        );
    }
}
