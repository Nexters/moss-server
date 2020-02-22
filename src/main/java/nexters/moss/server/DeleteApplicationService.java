package nexters.moss.server;

import lombok.AllArgsConstructor;
import nexters.moss.server.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class DeleteApplicationService {
    private HabitRecordRepository habitRecordRepository;
    private HabitRepository habitRepository;
    private WholeCakeRepository wholeCakeRepository;
    private ReportRepository reportRepository;
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;

    public void deleteAllRelationByUserId(Long userId) {
        habitRecordRepository.deleteByUser_Id(userId);
        habitRepository.deleteByUser_Id(userId);
        pieceOfCakeReceiveRepository.deleteByUser_Id(userId);
        pieceOfCakeSendRepository.deleteByUser_Id(userId);
        wholeCakeRepository.deleteByUser_Id(userId);
        reportRepository.deleteByUser_Id(userId);
    }
}
