package nexters.moss.server.web;

import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("habit")
public class HabitController {
    private HabitApplicationService habitApplicationService;

    public HabitController(HabitApplicationService habitApplicationService) {
        this.habitApplicationService = habitApplicationService;
    }

    // TODO: JWT Authentication will give user information
    @GetMapping("/history/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<List<HabitHistory>> getHabitHistory(@PathVariable Long userId) {
        return habitApplicationService.getHabitHistory(userId);
    }

    // TODO: JWT Authentication will give user information
    @PostMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<HabitHistory> createHabit(
            @PathVariable Long userId,
            @RequestBody Long habitId
    ) {
        return habitApplicationService.createHabit(userId, habitId);
    }

    // TODO: JWT Authentication will give user information
    @DeleteMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<Long> deleteHabit(
            @PathVariable Long userId,
            @RequestBody Long habitId
    ) {
        return habitApplicationService.deleteHabit(userId, habitId);
    }
}
