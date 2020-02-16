package nexters.moss.server.web;

import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.web.value.CategoryRequest;
import nexters.moss.server.web.value.HabitReqeust;
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
            @RequestBody CategoryRequest categoryRequest
    ) {
        return habitApplicationService.createHabit(userId, categoryRequest.getCategoryId());
    }

    // TODO: JWT Authentication will give user information
    @DeleteMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<Long> deleteHabit(
            @RequestParam Long userId,
            @RequestBody HabitReqeust habitReqeust
    ) {
        return habitApplicationService.deleteHabit(userId, habitReqeust.getHabitId());
    }

    // TODO: JWT Authentication will give user information
    @PutMapping("/record")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<HabitHistory> doneHabit(@RequestBody HabitReqeust habitReqeust) {
        return habitApplicationService.doneHabit(habitReqeust.getHabitId());
    }
}
