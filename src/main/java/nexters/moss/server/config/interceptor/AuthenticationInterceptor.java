package nexters.moss.server.config.interceptor;

import nexters.moss.server.domain.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader("accessToken");
        request.setAttribute("accessToken", accessToken);

        if(request.getRequestURI().equals("/api/user") && request.getMethod().equals("POST")) {
            return super.preHandle(request, response, handler);
        }

        String habikeryToken = request.getHeader("habikeryToken");
        Long userId = authenticationService.authenticate(habikeryToken);
        request.setAttribute("userId", userId);

        return super.preHandle(request, response, handler);
    }
}
