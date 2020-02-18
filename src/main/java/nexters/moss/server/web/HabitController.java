package nexters.moss.server.web;

import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.application.dto.HabitDoneResponse;
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
    private HttpServletRequest httpServletRequest;

    public HabitController(
            HabitApplicationService habitApplicationService,
            HttpServletRequest httpServletRequest
    ) {
        this.habitApplicationService = habitApplicationService;
        this.httpServletRequest = httpServletRequest;
    }

    // TODO: JWT Authentication will give user information
    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<List<HabitHistory>> getHabit() {
        return habitApplicationService.getHabit(
                (Long) httpServletRequest.getAttribute("userId")
        );
    }

    // TODO: JWT Authentication will give user information
    @PostMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<HabitHistory> createHabit(
            @RequestBody CategoryRequest categoryRequest
    ) {
        return habitApplicationService.createHabit(
                (Long) httpServletRequest.getAttribute("userId"),
                categoryRequest.getCategoryId()
        );
    }

    // TODO: JWT Authentication will give user information
    @DeleteMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<Long> deleteHabit(
            @RequestBody HabitReqeust habitReqeust,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        return habitApplicationService.deleteHabit(userId, habitReqeust.getHabitId());
    }

    // TODO: JWT Authentication will give user information
    @PutMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Response<HabitDoneResponse> doneHabit(
            @RequestBody HabitReqeust habitReqeust
    ) {
        return habitApplicationService.doneHabit(
//                (Long) httpServletRequest.getAttribute("userId"),
                2L,
                habitReqeust.getHabitId()
        );
    }
}
