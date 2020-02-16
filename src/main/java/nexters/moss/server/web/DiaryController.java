package nexters.moss.server.web;

import nexters.moss.server.application.DiaryApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.application.dto.diary.DiaryDTO;
import nexters.moss.server.application.dto.diary.HistoryResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diary")
public class DiaryController {
    private DiaryApplicationService diaryApplicationService;

    public DiaryController(DiaryApplicationService diaryApplicationService) {
        this.diaryApplicationService = diaryApplicationService;
    }

    @GetMapping("/piece")
    public Response<List<DiaryDTO>> getPieceOfCakeDiary(@RequestParam Long userId) {
        return diaryApplicationService.getPieceOfCakeDiary(userId);
    }

    @GetMapping("/whole")
    public Response<List<DiaryDTO>> getWholeCakeDiary(@RequestParam Long userId) {
        return diaryApplicationService.getWholeCakeDiary(userId);
    }

    @GetMapping("/history")
    public Response<HistoryResponse>getCakeHistory(@RequestParam Long userId, @RequestParam Long categoryId){
        return diaryApplicationService.getCakeHistory(userId, categoryId);
    }
}
