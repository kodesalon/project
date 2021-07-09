package com.project.kodesalon.common.interceptor;

import com.project.kodesalon.common.JwtManager;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.project.kodesalon.common.ErrorCode.INVALID_HEADER;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    public static final String LOGIN_MEMBER = "loginMember";
    private static final int BEARER_LENGTH = 7;
    private static final String LOG_ID = "logId";

    private final JwtManager jwtManager;

    public LoginInterceptor(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uuid = UUID.randomUUID().toString();
        String requestURI = request.getRequestURI();
        MDC.put(LOG_ID, uuid);
        log.info("REQUEST : [logId : {}] [requestURI : {}] [handler : {}]", uuid, requestURI, handler);

        String token = parseTokenFrom(request);
        jwtManager.validateToken(token);
        Long memberId = jwtManager.getMemberIdFrom(token);
        request.setAttribute(LOGIN_MEMBER, memberId);
        return true;
    }

    private String parseTokenFrom(HttpServletRequest request) {
        try {
            return request.getHeader("Authorization").substring(BEARER_LENGTH);
        } catch (NullPointerException e) {
            throw new JwtException(INVALID_HEADER);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestURI = request.getRequestURI();
        String logId = MDC.get(LOG_ID);
        log.info("RESPONSE : [logId : {}] [requestURI : {}] [handler : {}]", logId, requestURI, handler);
        MDC.clear();
        if (ex != null) {
            log.error("afterCompletion error : ", ex.getMessage());
        }
    }
}
