package nexters.moss.server.web;

import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.dto.cake.*;
import nexters.moss.server.application.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cake")
public class CakeController {
    private CakeApplicationService cakeApplicationService;

    public CakeController(CakeApplicationService cakeApplicationService) {
        this.cakeApplicationService = cakeApplicationService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Response<Long> createANewCake(@ModelAttribute CreateANewCakeRequest createANewCakeRequest) {
        return cakeApplicationService.createANewCake(createANewCakeRequest);
    }

    @GetMapping(value = "{userId}", params = "categoryId")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<NewCakeDTO> getANewCake(@PathVariable Long userId, @RequestParam Long categoryId) {
        return cakeApplicationService.getANewCake(userId, categoryId);
    }
}
