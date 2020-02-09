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

    @PostMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response addCake(@PathVariable Long userId, @RequestBody Map<String, Object> param){

        return null;
    }
}
