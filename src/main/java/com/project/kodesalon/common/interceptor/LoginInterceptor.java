package com.project.kodesalon.common.interceptor;

import com.project.kodesalon.common.JwtUtils;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final int BEARER_LENGTH = 7;
    private static final String LOG_ID = "logId";

    private final JwtUtils jwtUtils;
    private final MemberService memberService;

    public LoginInterceptor(JwtUtils jwtUtils, MemberService memberService) {
        this.jwtUtils = jwtUtils;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = UUID.randomUUID().toString();
        StringBuffer requestURI = request.getRequestURL();
        request.setAttribute(LOG_ID, uuid);
        log.info("REQUEST : [logId : {}] [requestURI : {}] [handler : {}]", uuid, requestURI, handler);

        String token = request.getHeader("Authorization").substring(BEARER_LENGTH);

        if (!jwtUtils.validateToken(token)) {
            return false;
        }

        Long memberId = jwtUtils.getMemberIdFrom(token);
        Member member = memberService.findById(memberId);
        request.setAttribute("loginMember", member);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);
        log.info("RESPONSE : [logId : {}] [requestURI : {}] [handler : {}]", logId, requestURI, handler);

        if (ex != null) {
            log.info("afterCompletion error : ", ex.getMessage());
        }
    }
}
