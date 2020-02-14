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

    @GetMapping("/piece/{userId}")
    public Response<List<DiaryDTO>> getAPieceOfCakeDiary(@PathVariable Long userId) {
        return diaryApplicationService.getAPieceOfCakeDiary(userId);
    }

    @GetMapping("/whole/{userId}")
    public Response<List<DiaryDTO>> getWholeCakeDiary(@PathVariable Long userId) {
        return diaryApplicationService.getWholeCakeDiary(userId);
    }

    @GetMapping("/history/{userId}")
    public Response<HistoryResponse>getCakeHistory(@PathVariable Long userId, @RequestParam Long categoryId){
        return diaryApplicationService.getCakeHistory(userId, categoryId);
    }
}
