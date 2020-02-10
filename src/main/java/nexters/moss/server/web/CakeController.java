package nexters.moss.server.web;

import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("cake")
public class CakeController {
    private CakeApplicationService cakeApplicationService;

    public CakeController(CakeApplicationService cakeApplicationService, UserRepository userRepository) {
        this.cakeApplicationService = cakeApplicationService;
    }

    @GetMapping("/categories")
    @ResponseStatus(value = HttpStatus.OK)
    public Response getAllHabits() {
        return cakeApplicationService.getAllHabits();
    }

    @PostMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Response addSentPieceOfCake(@PathVariable Long userId, @RequestBody Map<String, Object> data){
        return cakeApplicationService.addSentPieceOfCake(userId, data);
    }


    @PostMapping(value = "/{userId}", params = "categoryId")
    @ResponseStatus(value = HttpStatus.OK)
    public Response addReceivedPieceOfCake(@PathVariable Long userId, @RequestParam long categoryId){
        return cakeApplicationService.addReceivedPieceOfCake(userId, categoryId);
    }
}
