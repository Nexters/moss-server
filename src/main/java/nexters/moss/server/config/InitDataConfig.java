package nexters.moss.server.config;

import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.application.dto.cake.CreateNewCakeRequest;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.HabitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Configuration
public class InitDataConfig {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DescriptionRepository descriptionRepository;
    @Autowired
    HabitRepository habitRepository;
    @Autowired
    HabitRecordRepository habitRecordRepository;
    @Autowired
    PieceOfCakeSendRepository pieceOfCakeSendRepository;

    @Autowired
    HabitApplicationService habitApplicationService;
    @Autowired
    CakeApplicationService cakeApplicationService;

    @PostConstruct
    public void initData() {
        List<HabitType> habitTypes = Arrays.asList(HabitType.values());
        List<CakeType> cakeTypes = Arrays.asList(CakeType.values());

        for (int i = 1; i <= habitTypes.size(); i++) {
            Category category = categoryRepository.save(new Category(Integer.toUnsignedLong(i), habitTypes.get(i - 1), cakeTypes.get(i - 1)));
            descriptionRepository.save(new Description(null, category, "receive" + i, "diary" + i));
        }

        User sender = userRepository.save(userRepository.save(new User(null, 1234L, "accountToken", "sender", null)));
        User receiver = userRepository.save(userRepository.save(new User(null, 1235L, "accountToken", "receiver", null)));

        Category category = categoryRepository.findById(Integer.toUnsignedLong(1)).orElseThrow(() -> new IllegalArgumentException("No Matched Category"));

        habitApplicationService.createHabit(sender.getId(), category.getId());
        habitApplicationService.createHabit(receiver.getId(), category.getId());

        cakeApplicationService.sendCake(
                sender.getId(),
                new CreateNewCakeRequest(
                        category.getId(),
                        "sender send message"
                ));
    }
}