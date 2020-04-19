package nexters.moss.server.infra.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nexters.moss.server.config.exception.UnauthorizedException;
import nexters.moss.server.domain.user.SocialTokenService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KakaoTokenService implements SocialTokenService {
    private String requestUrl;
    private ObjectMapper objectMapper;

    // TODO: export to config file
    public KakaoTokenService(ObjectMapper objectMapper) {
        this.requestUrl = "https://kapi.kakao.com/v1/user";
        this.objectMapper = objectMapper;
    }

    @Override
    public Long getSocialUserId(String accessToken) throws UnauthorizedException {
        if(accessToken == null) {
            throw new UnauthorizedException("Access Token is null");
        }

        return httpTemplate(accessToken, (client, mapper) -> {
            HttpGet get = new HttpGet(requestUrl + "/access_token_info");
            get.addHeader("Authorization", "Bearer " + accessToken);

            HttpResponse response = client.execute(get);
            JsonNode jsonNode = mapper.readTree(response.getEntity().getContent());

            return jsonNode.path("id").asLong();
        });
    }

    private <T>T httpTemplate(String accessToken, Http<T> http) {
        HttpClient client = HttpClientBuilder.create().build();

        try {
            return http.get(client, objectMapper);
        } catch (IOException e) {
            throw new UnauthorizedException("No Matched Social User");
        }
    }
}
