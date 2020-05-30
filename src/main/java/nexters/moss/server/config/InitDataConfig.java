package nexters.moss.server.config;

import nexters.moss.server.application.CakeApplicationService;
import nexters.moss.server.application.HabitApplicationService;
import nexters.moss.server.domain.Category;
import nexters.moss.server.domain.cake.SentPieceOfCake;
import nexters.moss.server.domain.cake.SentPieceOfCakeRepository;
import nexters.moss.server.domain.habit.HabitRepository;
import nexters.moss.server.domain.user.User;
import nexters.moss.server.domain.user.UserRepository;
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
                        User.builder().habikeryToken("dummyUser").nickname("라이카").build(),
                        User.builder().habikeryToken("dummyUser").nickname("지니").build(),
                        User.builder().habikeryToken("dummyUser").nickname("이끼").build(),
                        User.builder().habikeryToken("dummyUser").nickname("태정태세비욘세").build()
                )
        );

        // init message
        sentPieceOfCakeRepository.saveAll(
                Arrays.asList(
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(0).getId()).note("물 마실수록 피부 촉촉!!").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(0).getId()).note("미세먼지 나쁠수록 물많이").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(0).getId()).note("저도 응원하면서 한잔~").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(1).getId()).note("꾸준히 화이팅~~!!").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(1).getId()).note("@@몸이 건강해진드아!").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(1).getId()).note("하다보면 점점 늘거에요").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(2).getId()).note("바쁜하루 마음 비우기").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(2).getId()).note("수고했어요 오늘도!").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(2).getId()).note("뭐든 다 잘될거에요^^").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(3).getId()).note("화이팅!! 할 수 있어요!").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(3).getId()).note("아침을 상쾌하게 시작!").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(3).getId()).note("파워워킹@@").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(4).getId()).note("오늘은 무슨일이??").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(4).getId()).note("똑똑해지는거 같아요ㅋㅋ").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(4).getId()).note("트렌드리더!!").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(5).getId()).note("아침을 먹어야 든든하죠~").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(5).getId()).note("오늘은 밥을 먹어봅시다!").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(5).getId()).note("대충이라도 차려먹어요~").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(6).getId()).note("하루를 정리하는 시간~~").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(6).getId()).note("고민을 쓰니까 후련해요").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(6).getId()).note("자기 전 이시간이 행복").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("마음의 양식을 쌓아보자!!").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("독서하는 당신 최고에요~").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("책읽기 힘든데 대단해요").build(),

                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(0).getId()).note("공복엔 따뜻한 물!").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(0).getId()).note("한잔씩 늘려나가요~~").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(0).getId()).note("꾸준히 습관 만들어나가요").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(1).getId()).note("하루 5분만이라도 꼭꼭!").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(1).getId()).note("같이 건강해져요 ㅎㅎ").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(1).getId()).note("몸도 유연 마음도 유연").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(2).getId()).note("꾸준히 해봐요 우리~!").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(2).getId()).note("피스~~~").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(2).getId()).note("스트레스 날려요!").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(3).getId()).note("구름이 이뻐요!").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(3).getId()).note("움직이니까 힘나요!").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(3).getId()).note("날씨좋아서 기분좋아요").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(4).getId()).note("오늘도 해봅시당~").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(4).getId()).note("매일하니까 뿌듯하당!").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(4).getId()).note("가끔 영어뉴스도 좋아요").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(5).getId()).note("호랑이 기운 뿜뿜 :D").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(5).getId()).note("상쾌한 아침입니다!").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(5).getId()).note("꾸준히 조금씩 화이팅@@").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(6).getId()).note("고요한 밤 끄적끄적").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(6).getId()).note("마음의 안정~~~").build(),
                        SentPieceOfCake.builder().userId(users.get(1).getId()).categoryId(categories.get(6).getId()).note("꾸준히 한장씩 써봐요").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("책읽고 마음부자되자").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("그림책 보시고 힐링하세요").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("전자책도 볼만해요~").build(),

                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(0).getId()).note("크으~ 잘하고있어요:D").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(0).getId()).note("수고했어요 오늘도!").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(0).getId()).note("오늘도 화이팅!!ㅎㅎ").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(0).getId()).note("잘하고 있어요~~").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(1).getId()).note("잘하고 있어요 ㅎㅎ").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(1).getId()).note("수고했어요 오늘도~").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(1).getId()).note("홧팅!!").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(1).getId()).note("쭉쭉 늘려보아용!").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(2).getId()).note("걱정고민을 버려봅시다").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(2).getId()).note("오늘도 고생많았어요").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(2).getId()).note("천천히해도 괜찮아요").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(2).getId()).note("화이팅!! 할 수 있어요!").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(3).getId()).note("우리 같이 건강해져요~").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(3).getId()).note("이제 침대를 벗어나봅시다").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(3).getId()).note("꾸준히 화이팅~ㅎㅎ").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(3).getId()).note("잘하고 있어요*").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(4).getId()).note("꾸준히 화이팅해요~!").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(4).getId()).note("대화에 끼려면 필수!").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(4).getId()).note("화이팅!! 할 수 있어요!").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(4).getId()).note("잘하고 있어요 !!!!").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(5).getId()).note("일찍일어나는새가 아침먹음").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(5).getId()).note("한국인은 밥심").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(5).getId()).note("꾸준히 해봐요 우리").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(5).getId()).note("든든한 아침으로 시작!!").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(6).getId()).note("한줄이라도 좋아요!!").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(6).getId()).note("오늘 하루도 수고했어요").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(6).getId()).note("언제나 화이팅!!").build(),
                        SentPieceOfCake.builder().userId(users.get(2).getId()).categoryId(categories.get(6).getId()).note("할수있다!!!").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("일주일에 한권 도저어언!!").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("유튜브 줄이고 독서해요 !").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("소설 읽기 좋은 날씨 :)").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("얇은 책부터 차근차근").build(),

                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(0).getId()).note("찬물 한번에 마시면 탈나욤").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(0).getId()).note("한달짼데  때깔이 달라요").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(0).getId()).note("물이 별로면 보리차").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(1).getId()).note("낙타자세 (ˆ̑‵̮ˆ̑)").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(1).getId()).note("거북목 고칩시당").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(1).getId()).note("아차 싶을때 자리교정하세요").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(2).getId()).note("이너피스!").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(2).getId()).note("시간없으면 수면명상도  굿").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(2).getId()).note("유튜브로 시작해요").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(3).getId()).note("윤종신노래 수목원에서 추천").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(3).getId()).note("우울할틈이 없어요 진짜 ").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(3).getId()).note("하루 만보를 위해서 밤산책").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(4).getId()).note("일단 듣기라도 하니 좋아요").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(4).getId()).note("듣다보면 흐름이 보여요:)").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(4).getId()).note("시사몰라서 창피했었어요ㅠㅠ").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(5).getId()).note("정신력 맑게하기!").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(5).getId()).note("사과한개 꼭 먹고있어요:)").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(5).getId()).note("부담스럽지 않게만 !").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(6).getId()).note("하루 한줄 날위한 시간").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(6).getId()).note("오늘 하루도 고생했어요:)").build(),
                        SentPieceOfCake.builder().userId(users.get(3).getId()).categoryId(categories.get(6).getId()).note("내일을 위한 다짐일기").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("최애책은 어떤책인가요?").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("출퇴근때 책 읽기 좋아요").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("외출못하니까 책이라도 읽자").build(),

                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(0).getId()).note("물먹는 하마되는중 ㅠㅠ").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(0).getId()).note("화장실 많이가도 킵고잉").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(0).getId()).note("따뜻한물이 좋더라구요").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(1).getId()).note("아침에 하는게 좋아요").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(1).getId()).note("하루 마무리할때 오분만하기").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(1).getId()).note("폼롤러로 하면진짜 시원해요").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(2).getId()).note("나와의 대화 진짜좋아요:)").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(2).getId()).note("모두 내려놓는 과정입니다").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(2).getId()).note("마음 가득히 행복해지기").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(3).getId()).note("길이  있으니 걷는다.").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(3).getId()).note("생각정리엔 산책이 최고죠").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(3).getId()).note("여름밤 산책 낭만적이죠?").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(4).getId()).note("요즘은 더더욱 들어야죠 ").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(4).getId()).note("회사에선 스몰토크 주도자").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(4).getId()).note("말도 잘하게 되는거같아요.").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(5).getId()).note("하루의 시작은 든든하게").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(5).getId()).note("점심에 많이 안먹게돼요").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(5).getId()).note("늦잠을 안자게 돼서 좋아").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(6).getId()).note("오늘도 고생했어 나야 !!").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(6).getId()).note("고이접는 하루, 밤의 작가").build(),
                        SentPieceOfCake.builder().userId(users.get(4).getId()).categoryId(categories.get(6).getId()).note("좋아하는 노래 들으며 끄적").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("시집도 좋아요 :)").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("자기계발서 보고 자극받는다").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("추리소설 못끊겠어!!").build(),

                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(0).getId()).note(" ͡° ͜ʖ ͡°  적셔").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(0).getId()).note("자리끼가 꿀맛이던데").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(0).getId()).note("ꉂꉂ(ᵔᗜᵔ*) 화이팅이야").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(0).getId()).note("마니 마시다보면 더 마심").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(1).getId()).note("운동안해도 날씬해진 기분").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(1).getId()).note("안하면 몸굳으니 얼른해요").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(1).getId()).note("자존감 올라가는거 같아요").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(1).getId()).note("키도 크는 기분 ^^...").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(2).getId()).note("명상하는 사람 진짜 멋져요").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(2).getId()).note("처음만 어려운거 같아요").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(2).getId()).note("충만한 삶을 위해서 :)").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(2).getId()).note("오늘 하루도 수고했어요.").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(3).getId()).note("밤산책은 시인 저리가라에요").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(3).getId()).note("낮에도 하고싶지만 회사ㅠㅠ").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(3).getId()).note("저는 15000보 걸었어요").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(3).getId()).note("3만보 걸으니 다리 아작남").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(4).getId()).note("업무랑 상관없는게 없어요.").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(4).getId()).note("답답하기도 한데 ㅠ좋아요").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(4).getId()).note("출근길에 몇개씩만이라도 !").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(4).getId()).note("똑똑해지는 기분~").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(5).getId()).note("여유로운 출근길이 돼요:)").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(5).getId()).note("냠냠 :) 당신도 냠냠?").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(5).getId()).note("오늘은 뭐먹었어요?").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(5).getId()).note("공복엔 커피 먹지마요 !!").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(6).getId()).note("자신한테 취하는 시간이에요").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(6).getId()).note("힘들땐 단어라도 써봐용").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(6).getId()).note("점점 글을 잘쓰는거 같아요").build(),
                        SentPieceOfCake.builder().userId(users.get(5).getId()).categoryId(categories.get(6).getId()).note("크..나 진짜 멋지다..").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("3회독 도전중 ~.~").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("시험기간엔 책읽고 싶어ㅠㅠ").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("간접경험 끝판왕 독서!").build(),
                        SentPieceOfCake.builder().userId(users.get(0).getId()).categoryId(categories.get(7).getId()).note("이 책은 꼭 다 읽자!!").build()
                )
        );
    }
}