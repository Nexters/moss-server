package nexters.moss.server.web;

import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.application.dto.HabitDoneResponse;
import nexters.moss.server.application.dto.HabitHistory;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.web.value.CategoryRequest;
import nexters.moss.server.web.value.HabitOrderRequest;
import nexters.moss.server.web.value.HabitRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/habit")
public class HabitController {
    private HabitApplicationService habitApplicationService;
    private HttpServletRequest httpServletRequest;

    public HabitController(
            HabitApplicationService habitApplicationService,
            HttpServletRequest httpServletRequest
    ) {
        this.habitApplicationService = habitApplicationService;
        this.httpServletRequest = httpServletRequest;
    }

    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<List<HabitHistory>> getHabit() {
        return habitApplicationService.getHabit(
                (Long) httpServletRequest.getAttribute("userId")
        );
    }

    @PostMapping("")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Response<HabitHistory> createHabit(
            @RequestBody CategoryRequest categoryRequest
    ) {
        return habitApplicationService.createHabit(
                (Long) httpServletRequest.getAttribute("userId"),
                categoryRequest.getCategoryId()
        );
    }

    @DeleteMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<Long> deleteHabit(
            @RequestBody HabitRequest habitRequest,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        return habitApplicationService.deleteHabit(userId, habitRequest.getHabitId());
    }

    @PutMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public HabitDoneResponse doneHabit(
            @RequestBody HabitRequest habitRequest
    ) {
        return habitApplicationService.doneHabit(
                (Long) httpServletRequest.getAttribute("userId"),
                habitRequest.getHabitId()
        );
    }

    @PutMapping("/order")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<Long> changeHabitOrder(
            @RequestBody HabitOrderRequest habitOrderRequest
    ) {
        return habitApplicationService.changeHabitOrder(
                (Long) httpServletRequest.getAttribute("userId"),
                habitOrderRequest.getHabitId(),
                habitOrderRequest.getOrder()
        );
    }
}
