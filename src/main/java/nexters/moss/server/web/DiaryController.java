package nexters.moss.server.web;

import nexters.moss.server.application.DiaryApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.diary.DiaryDTO;
import nexters.moss.server.application.dto.diary.HistoryResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/diary")
public class DiaryController {
    private DiaryApplicationService diaryApplicationService;
    private HttpServletRequest httpServletRequest;

    public DiaryController(
            DiaryApplicationService diaryApplicationService,
            HttpServletRequest httpServletRequest
    ) {
        this.httpServletRequest = httpServletRequest;
        this.diaryApplicationService = diaryApplicationService;
    }

    @GetMapping("/piece")
    public Response<List<DiaryDTO>> getPieceOfCakeDiary() {
        return diaryApplicationService.getPieceOfCakeDiary(
                (Long)httpServletRequest.getAttribute("userId")
        );
    }

    @GetMapping("/whole")
    public Response<List<DiaryDTO>> getWholeCakeDiary() {
        return diaryApplicationService.getWholeCakeDiary(
                (Long)httpServletRequest.getAttribute("userId")
        );
    }

    @GetMapping("/history")
    public Response<HistoryResponse>getCakeHistory(@RequestParam Long categoryId){
        return diaryApplicationService.getCakeHistory(
                (Long)httpServletRequest.getAttribute("userId"),
                categoryId
        );
    }
}
