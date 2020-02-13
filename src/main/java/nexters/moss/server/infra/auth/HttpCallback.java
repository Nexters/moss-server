package nexters.moss.server.infra.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;

import java.io.IOException;

public interface HttpCallback<T> {
    public T getHttpData(HttpClient client, ObjectMapper mapper) throws IOException;
}
