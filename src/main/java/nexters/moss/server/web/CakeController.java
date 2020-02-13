package nexters.moss.server.web;

import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.dto.cake.CreateANewCakeRequest;
import nexters.moss.server.application.dto.cake.CreateANewCakeResponse;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.cake.GetANewCakeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cake")
public class CakeController {
    private CakeApplicationService cakeApplicationService;

    public CakeController(CakeApplicationService cakeApplicationService) {
        this.cakeApplicationService = cakeApplicationService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Response<CreateANewCakeResponse> CreateANewCake(@ModelAttribute CreateANewCakeRequest createANewCakeRequest){
        return cakeApplicationService.CreateANewCake(createANewCakeRequest);
    }


    @PostMapping(value = "/{userId}", params = "habitId")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<GetANewCakeResponse> GetANewCake(@PathVariable Long userId, @RequestParam long habitId){
        return cakeApplicationService.GetANewCake(userId, habitId);
    }
}
