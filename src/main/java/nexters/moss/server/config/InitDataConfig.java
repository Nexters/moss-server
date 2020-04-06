package nexters.moss.server.config;

import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.HabitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("init")
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
    public void init() {
        // init category
        List<Category> categories = categoryRepository.saveAll(
                Arrays.asList(
                        Category.builder().habitType(HabitType.WATER).cakeType(CakeType.WATERMELON).build(),
                        Category.builder().habitType(HabitType.STRETCHING).cakeType(CakeType.CHEESE).build(),
                        Category.builder().habitType(HabitType.MEDITATION).cakeType(CakeType.WHIPPING_CREAM).build(),
                        Category.builder().habitType(HabitType.WALK).cakeType(CakeType.GREEN_TEA).build(),
                        Category.builder().habitType(HabitType.NEWS).cakeType(CakeType.COFFEE).build(),
                        Category.builder().habitType(HabitType.BREAKFAST).cakeType(CakeType.APPLE).build(),
                        Category.builder().habitType(HabitType.DIARY).cakeType(CakeType.CHESTNUT).build(),
                        Category.builder().habitType(HabitType.READING).cakeType(CakeType.WALNUT).build()
                )
        );

        descriptionRepository.saveAll(
                Arrays.asList(
                        Description.builder().category(categories.get(0)).receivePieceOfCake("물 마시는 나에게").diary("수분충전").build(),
                        Description.builder().category(categories.get(1)).receivePieceOfCake("스트레칭하는 나에게").diary("쭉쭉 늘어나는").build(),
                        Description.builder().category(categories.get(2)).receivePieceOfCake("명상하는 나에게").diary("부드러운 마음").build(),
                        Description.builder().category(categories.get(3)).receivePieceOfCake("산책하는 나에게").diary("싱그러운").build(),
                        Description.builder().category(categories.get(4)).receivePieceOfCake("뉴스보는 나에게").diary("아침을 깨우는").build(),
                        Description.builder().category(categories.get(5)).receivePieceOfCake("아침먹는 나에게").diary("하루의 시작").build(),
                        Description.builder().category(categories.get(6)).receivePieceOfCake("책읽는 나에게").diary("나를 위한 시간").build(),
                        Description.builder().category(categories.get(7)).receivePieceOfCake("일기쓰는 나에게").diary("마음의 양").build()

                )
        );

        // init user
        List<User> users = userRepository.saveAll(
                Arrays.asList(
                        User.builder().habikeryToken("dummyUser").nickname("정민츄").build(),
                        User.builder().habikeryToken("dummyUser").nickname("인절미").build(),
                        User.builder().habikeryToken("dummyUser").nickname("라이카").build()
                )
        );

        // init message
        pieceOfCakeSendRepository.saveAll(
                Arrays.asList(
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(0)).note("물 마실수록 피부 촉촉!!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(0)).note("미세먼지 나쁠수록 물많이!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(0)).note("저도 응원하면서 한잔~").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(1)).note("꾸준히 화이팅~~!!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(1)).note("@@몸이 건강해진드아!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(1)).note("하다보면 점점 늘거에요").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(2)).note("바쁜하루 마음 비우기").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(2)).note("수고했어요 오늘도!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(2)).note("뭐든 다 잘될거에요^^").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(3)).note("화이팅!! 할 수 있어요!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(3)).note("아침을 상쾌하게 시작!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(3)).note("파워워킹@@").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(4)).note("오늘은 무슨일이??").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(4)).note("똑똑해지는거 같아요ㅋㅋ").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(4)).note("트렌드리더!!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(5)).note("아침을 먹어야 든든하죠~").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(5)).note("오늘은 밥을 먹어봅시다!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(5)).note("대충이라도 차려먹어요~").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(6)).note("하루를 정리하는 시간~~").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(6)).note("고민을 쓰니까 후련해요").build(),
                        SentPieceOfCake.builder().user(users.get(0)).category(categories.get(6)).note("자기 전 이시간이 행복").build(),

                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(0)).note("공복엔 따뜻한 물!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(0)).note("한잔씩 늘려나가요~~").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(0)).note("꾸준히 습관 만들어나가요").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(1)).note("하루 5분만이라도 꼭꼭!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(1)).note("같이 건강해져요 ㅎㅎ").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(1)).note("몸도 유연 마음도 유연").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(2)).note("꾸준히 해봐요 우리~!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(2)).note("피스~~~").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(2)).note("스트레스 날려요!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(3)).note("구름이 이뻐요!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(3)).note("움직이니까 힘나요!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(3)).note("날씨가 좋아서 기분도좋아요").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(4)).note("오늘도 해봅시당~").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(4)).note("매일하니까 뿌듯하당!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(4)).note("가끔은 영어뉴스도 좋아요").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(5)).note("호랑이 기운 뿜뿜 :D").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(5)).note("상쾌한 아침입니다!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(5)).note("꾸준히 조금씩 화이팅@@").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(6)).note("고요한 밤 끄적끄적").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(6)).note("마음의 안정~~~").build(),
                        SentPieceOfCake.builder().user(users.get(1)).category(categories.get(6)).note("꾸준히 한장씩 써봐요").build(),

                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(0)).note("크으~ 잘하고있어요:D").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(0)).note("수고했어요 오늘도!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(0)).note("오늘도 화이팅!!ㅎㅎ").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(0)).note("잘하고 있어요~~").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(1)).note("잘하고 있어요 ㅎㅎ").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(1)).note("수고했어요 오늘도~").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(1)).note("홧팅!!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(1)).note("쭉쭉 늘려보아용!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(2)).note("걱정고민을 버려봅시다").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(2)).note("오늘도 고생많았어요").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(2)).note("천천히해도 괜찮아요").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(2)).note("화이팅!! 할 수 있어요!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(3)).note("우리 같이 건강해져요~").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(3)).note("이제 침대를 벗어나봅시다").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(3)).note("꾸준히 화이팅~ㅎㅎ").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(3)).note("잘하고 있어요*").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(4)).note("꾸준히 화이팅해요~!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(4)).note("대화에 끼려면 필수!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(4)).note("화이팅!! 할 수 있어요!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(4)).note("잘하고 있어요 !!!!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(5)).note("일찍일어나는새가 아침먹음").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(5)).note("한국인은 밥심").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(5)).note("꾸준히 해봐요 우리").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(5)).note("든든한 아침으로 시작!!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(6)).note("한줄이라도 좋아요!!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(6)).note("오늘 하루도 수고했어요").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(6)).note("언제나 화이팅!!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).category(categories.get(6)).note("할수있다!!!").build()
                )
        );
    }
}