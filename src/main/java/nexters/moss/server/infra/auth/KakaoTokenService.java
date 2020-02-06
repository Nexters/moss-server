package nexters.moss.server.infra.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nexters.moss.server.domain.model.SocialTokenService;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class KakaoTokenService implements SocialTokenService {
    private final static String requestUrl = "https://kapi.kakao.com/v2/user/me";
    private final static String clientId = "6bbaaf6b3996a8ea06861f94ffc48b7e";
    private final static String redirectUri = "";

    @Override
    public Long querySocialUserId(String accessToken) {
        JsonNode jsonData = getKakaoUserInfo(accessToken);

        return Long.parseLong(jsonData.path("id").asText());
    }

    private JsonNode getKakaoUserInfo(String accessToken) {
        HttpPost post = new HttpPost(requestUrl);
        post.addHeader("Authorization", "Bearer" + accessToken);

        HttpClient client = HttpClientBuilder.create().build();
        JsonNode returnNode = null;

        try {
            HttpResponse response = client.execute(post);
            int responseCode = response.getStatusLine().getStatusCode();
            System.out.println("ResponseCode: " + responseCode);

            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnNode;
    }
}
