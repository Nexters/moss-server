package nexters.moss.server.web;

import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public Response<List<HabitHistory>> getHabitHistory(@RequestParam Long userId) {
        return habitApplicationService.getHabitHistory(userId);
    }

    // TODO: JWT Authentication will give user information
    @PostMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<HabitHistory> createHabit(
            @RequestParam Long userId,
            @RequestBody Long categoryId
    ) {
        return habitApplicationService.createHabit(userId, categoryId);
    }

    // TODO: JWT Authentication will give user information
    @DeleteMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<Long> deleteHabit(
            @RequestParam Long userId,
            @RequestBody Long habitId
    ) {
        return habitApplicationService.deleteHabit(userId, habitId);
    }

    // TODO: JWT Authentication will give user information
    @PutMapping("/record")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<HabitHistory> doneHabit(@RequestBody Long habitId) {
        return habitApplicationService.doneHabit(habitId);
    }
}
