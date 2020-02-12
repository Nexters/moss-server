package nexters.moss.server.infra.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nexters.moss.server.domain.model.SocialTokenService;
import nexters.moss.server.domain.model.exception.NoSocialUserInfoException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class KakaoTokenService implements SocialTokenService {
    private  String requestUrl;

    // TODO: export to config file
    public KakaoTokenService() {
        this.requestUrl = "https://kapi.kakao.com/v2/user/me";
    }

    @Override
    public Long querySocialUserId(String accessToken) throws NoSocialUserInfoException {
        JsonNode jsonData = getKakaoUserInfo(accessToken);

        return Long.parseLong(jsonData.path("id").asText());
    }

    private JsonNode getKakaoUserInfo(String accessToken)  {
        HttpPost post = new HttpPost(requestUrl);
        post.addHeader("Authorization", "Bearer " + accessToken);

        HttpClient client = HttpClientBuilder.create().build();
        JsonNode returnNode = null;

        try {
            HttpResponse response = client.execute(post);

            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new NoSocialUserInfoException();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new NoSocialUserInfoException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new NoSocialUserInfoException();
        }

        return returnNode;
    }
}
