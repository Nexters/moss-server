package nexters.moss.server.infra.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nexters.moss.server.config.exception.SocialUserNotFoundException;
import nexters.moss.server.domain.service.SocialTokenService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KakaoTokenService implements SocialTokenService {
    private String requestUrl;
    private ObjectMapper objectMapper;

    // TODO: export to config file
    public KakaoTokenService(ObjectMapper objectMapper) {
        this.requestUrl = "https://kapi.kakao.com/v2/user/me";
        this.objectMapper = objectMapper;
    }

    @Override
    public Long getSocialUserId(String accessToken) throws SocialUserNotFoundException {
        if(accessToken == null) {
            throw new SocialUserNotFoundException("Access Token is null");
        }

        return httpTemplate(accessToken, (client, mapper) -> {
            HttpPost post = new HttpPost(requestUrl);
            post.addHeader("Authorization", "Bearer " + accessToken);

            HttpResponse response = client.execute(post);
            JsonNode jsonNode = mapper.readTree(response.getEntity().getContent());

            return jsonNode.path("id").asLong();
        });
    }

    private <T>T httpTemplate(String accessToken, Http<T> http) {
        HttpClient client = HttpClientBuilder.create().build();

        try {
            return http.get(client, objectMapper);
        } catch (IOException e) {
            throw new SocialUserNotFoundException("No Matched Social User");
        }
    }
}
