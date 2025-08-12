package sumcoda.boardbuddy.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import sumcoda.boardbuddy.exception.auth.AuthenticationMissingException;
import sumcoda.boardbuddy.util.AuthUtil;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthUtil authUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AuthenticationMissingException("로그인 하지 않은 사용자의 요청입니다.");
        }

        String username = authUtil.getUserNameByLoginType(authentication);

        request.setAttribute("username", username);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
