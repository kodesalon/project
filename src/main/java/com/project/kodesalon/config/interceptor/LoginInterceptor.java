package com.project.kodesalon.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionException;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

import static com.project.kodesalon.exception.ErrorCode.INVALID_SESSION;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    public static final String LOGIN_MEMBER = "loginMember";
    private static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflightRequest(request)) {
            return true;
        }

        String uuid = UUID.randomUUID().toString();
        String requestURI = request.getRequestURI();
        MDC.put(LOG_ID, uuid);
        log.info("REQUEST : [logId : {}] [requestURI : {}] [handler : {}]", uuid, requestURI, handler);

        HttpSession session = request.getSession(false);
        checkEmpty(session);

        return true;
    }

    private boolean isPreflightRequest(final HttpServletRequest request) {
        return request.getMethod().equals("OPTIONS");
    }

    private void checkEmpty(final HttpSession session) {
        if (session == null || session.getAttribute(LOGIN_MEMBER) == null) {
            throw new SessionException(INVALID_SESSION);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestURI = request.getRequestURI();
        String logId = MDC.get(LOG_ID);
        log.info("RESPONSE : [logId : {}] [requestURI : {}] [handler : {}]", logId, requestURI, handler);
        MDC.clear();

        if (ex != null) {
            log.error("afterCompletion error : {}", ex.getMessage());
        }
    }
}
