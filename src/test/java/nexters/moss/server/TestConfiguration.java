package nexters.moss.server;


import nexters.moss.server.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Configuration
public class TestConfiguration {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DescriptionRepository descriptionRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitRecordRepository habitRecordRepository;

    @Autowired
    private PieceOfCakeSendRepository pieceOfCakeSendRepository;

    @Autowired
    private PieceOfCakeReceiveRepository pieceOfCakeReceiveRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private WholeCakeRepository wholeCakeRepository;

    public void tearDown() {
        habitRecordRepository.deleteAll();
        pieceOfCakeReceiveRepository.deleteAll();
        pieceOfCakeSendRepository.deleteAll();
        wholeCakeRepository.deleteAll();
        habitRepository.deleteAll();
        reportRepository.deleteAll();
        userRepository.deleteAll();
        descriptionRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
