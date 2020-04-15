package nexters.moss.server.config;

import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.domain.model.*;
import nexters.moss.server.domain.repository.*;
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
    List<Category> categories;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HabitRepository habitRepository;

    @Autowired
    SentPieceOfCakeRepository sentPieceOfCakeRepository;

    @Autowired
    HabitApplicationService habitApplicationService;

    @Autowired
    CakeApplicationService cakeApplicationService;

    @PostConstruct
    public void init() {
        // init user
        List<User> users = userRepository.saveAll(
                Arrays.asList(
                        User.builder().habikeryToken("dummyUser").nickname("정민츄").build(),
                        User.builder().habikeryToken("dummyUser").nickname("인절미").build(),
                        User.builder().habikeryToken("dummyUser").nickname("라이카").build()
                )
        );

        // init message
        sentPieceOfCakeRepository.saveAll(
                Arrays.asList(
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(0).getId()).note("물 마실수록 피부 촉촉!!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(0).getId()).note("미세먼지 나쁠수록 물많이!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(0).getId()).note("저도 응원하면서 한잔~").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(1).getId()).note("꾸준히 화이팅~~!!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(1).getId()).note("@@몸이 건강해진드아!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(1).getId()).note("하다보면 점점 늘거에요").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(2).getId()).note("바쁜하루 마음 비우기").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(2).getId()).note("수고했어요 오늘도!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(2).getId()).note("뭐든 다 잘될거에요^^").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(3).getId()).note("화이팅!! 할 수 있어요!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(3).getId()).note("아침을 상쾌하게 시작!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(3).getId()).note("파워워킹@@").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(4).getId()).note("오늘은 무슨일이??").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(4).getId()).note("똑똑해지는거 같아요ㅋㅋ").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(4).getId()).note("트렌드리더!!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(5).getId()).note("아침을 먹어야 든든하죠~").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(5).getId()).note("오늘은 밥을 먹어봅시다!").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(5).getId()).note("대충이라도 차려먹어요~").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(6).getId()).note("하루를 정리하는 시간~~").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(6).getId()).note("고민을 쓰니까 후련해요").build(),
                        SentPieceOfCake.builder().user(users.get(0)).categoryId(categories.get(6).getId()).note("자기 전 이시간이 행복").build(),

                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(0).getId()).note("공복엔 따뜻한 물!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(0).getId()).note("한잔씩 늘려나가요~~").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(0).getId()).note("꾸준히 습관 만들어나가요").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(1).getId()).note("하루 5분만이라도 꼭꼭!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(1).getId()).note("같이 건강해져요 ㅎㅎ").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(1).getId()).note("몸도 유연 마음도 유연").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(2).getId()).note("꾸준히 해봐요 우리~!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(2).getId()).note("피스~~~").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(2).getId()).note("스트레스 날려요!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(3).getId()).note("구름이 이뻐요!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(3).getId()).note("움직이니까 힘나요!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(3).getId()).note("날씨가 좋아서 기분도좋아요").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(4).getId()).note("오늘도 해봅시당~").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(4).getId()).note("매일하니까 뿌듯하당!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(4).getId()).note("가끔은 영어뉴스도 좋아요").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(5).getId()).note("호랑이 기운 뿜뿜 :D").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(5).getId()).note("상쾌한 아침입니다!").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(5).getId()).note("꾸준히 조금씩 화이팅@@").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(6).getId()).note("고요한 밤 끄적끄적").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(6).getId()).note("마음의 안정~~~").build(),
                        SentPieceOfCake.builder().user(users.get(1)).categoryId(categories.get(6).getId()).note("꾸준히 한장씩 써봐요").build(),

                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(0).getId()).note("크으~ 잘하고있어요:D").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(0).getId()).note("수고했어요 오늘도!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(0).getId()).note("오늘도 화이팅!!ㅎㅎ").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(0).getId()).note("잘하고 있어요~~").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(1).getId()).note("잘하고 있어요 ㅎㅎ").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(1).getId()).note("수고했어요 오늘도~").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(1).getId()).note("홧팅!!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(1).getId()).note("쭉쭉 늘려보아용!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(2).getId()).note("걱정고민을 버려봅시다").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(2).getId()).note("오늘도 고생많았어요").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(2).getId()).note("천천히해도 괜찮아요").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(2).getId()).note("화이팅!! 할 수 있어요!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(3).getId()).note("우리 같이 건강해져요~").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(3).getId()).note("이제 침대를 벗어나봅시다").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(3).getId()).note("꾸준히 화이팅~ㅎㅎ").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(3).getId()).note("잘하고 있어요*").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(4).getId()).note("꾸준히 화이팅해요~!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(4).getId()).note("대화에 끼려면 필수!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(4).getId()).note("화이팅!! 할 수 있어요!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(4).getId()).note("잘하고 있어요 !!!!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(5).getId()).note("일찍일어나는새가 아침먹음").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(5).getId()).note("한국인은 밥심").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(5).getId()).note("꾸준히 해봐요 우리").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(5).getId()).note("든든한 아침으로 시작!!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(6).getId()).note("한줄이라도 좋아요!!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(6).getId()).note("오늘 하루도 수고했어요").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(6).getId()).note("언제나 화이팅!!").build(),
                        SentPieceOfCake.builder().user(users.get(2)).categoryId(categories.get(6).getId()).note("할수있다!!!").build()
                )
        );
    }
}