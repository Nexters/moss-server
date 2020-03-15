package nexters.moss.server.cake;

import nexters.moss.server.application.UserApplicationService;
import nexters.moss.server.application.dto.Response;
import nexters.moss.server.domain.model.Category;
import nexters.moss.server.domain.model.SentPieceOfCake;
import nexters.moss.server.domain.model.User;
import nexters.moss.server.domain.repository.CategoryRepository;
import nexters.moss.server.domain.repository.PieceOfCakeSendRepository;
import nexters.moss.server.domain.repository.UserRepository;
import nexters.moss.server.domain.service.SocialTokenService;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.HabitType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CakeApplicationControllerTest {
        @Autowired
        TestRestTemplate testRestTemplate;

        @LocalServerPort
        int randomServerPort;

        @Autowired
        private UserRepository userRepository;
        @Autowired
        private UserApplicationService userApplicationService;
        @Autowired
        private CategoryRepository categoryRepository;
        @Autowired
        private PieceOfCakeSendRepository pieceOfCakeSendRepository;

        @MockBean(name = "socialTokenService")
        private SocialTokenService socialTokenService;

        private String testAccessToken;
        private String habikeryToken;
        private User testUser;
        private Category category;

        @Before
        public void setup() {
            testAccessToken = "accessToken";
            testUser = User.builder()
                    .socialId(12345678L)
                    .nickname("nickname")
                    .build();

            given(socialTokenService.getSocialUserId(testAccessToken))
                    .willReturn(testUser.getSocialId());

            userRepository.save(testUser);
            habikeryToken = userApplicationService.login(testAccessToken).getData();

            List<HabitType> habitTypes = Arrays.asList(HabitType.values());
            List<CakeType> cakeTypes = Arrays.asList(CakeType.values());
            category = categoryRepository.save(new Category(null, habitTypes.get(0), cakeTypes.get(0)));
        }

        @Test
        public void sendCakeTest() throws URISyntaxException {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("habikeryToken", habikeryToken);

            URI uri = new URI("/cake");

            Map<String,Object> data = new HashMap<>();
            data.put("note", "test~!!");
            data.put("categoryId",category.getId());

            HttpEntity<Map<String,Object>> req = new HttpEntity<>(data, httpHeaders);

            Response<Integer> res = this.testRestTemplate.postForObject(uri, req, Response.class);
            assertThat(res).isNotNull();

            long pieceOfCakeId = res.getData().longValue();
            SentPieceOfCake sentPieceOfCake = pieceOfCakeSendRepository.findById(pieceOfCakeId).get();
            assertThat(data.get("note")).isEqualTo(sentPieceOfCake.getNote());
        }
}
