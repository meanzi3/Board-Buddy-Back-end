package sumcoda.boardbuddy.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

public class HttpMethodFilteringInterceptor implements HandlerInterceptor {

    private final HandlerInterceptor delegateInterceptor;

    private final Set<String> methodsToIntercept;

    public HttpMethodFilteringInterceptor(HandlerInterceptor delegateInterceptor, String... methodsToIntercept) {
        this.delegateInterceptor = delegateInterceptor;
        this.methodsToIntercept = Set.of(methodsToIntercept);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1) 감시할 메서드가 아니면 바로 통과
        if (!methodsToIntercept.contains(request.getMethod())) {
            return true;
        }
        // 2) 감시할 메서드일 때만 실제 인터셉터(인증) 로직 위임
        return delegateInterceptor.preHandle(request, response, handler);
    }

}
