package nexters.moss.server.web;

import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.application.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("habit")
public class HabitController {
    private HabitApplicationService habitApplicationService;

    public HabitController(HabitApplicationService habitApplicationService) {
        this.habitApplicationService = habitApplicationService;
    }

    // TODO: JWT Authentication will give user information
    @GetMapping("/history")
    @ResponseStatus(value = HttpStatus.OK)
    public Response getHabitHistory(@PathVariable Long userId) {
        return habitApplicationService.getHabitHistory(userId);
    }
}
