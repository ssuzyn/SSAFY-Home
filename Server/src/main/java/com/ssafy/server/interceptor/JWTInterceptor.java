package com.ssafy.server.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ssafy.server.exception.UnAuthorizedException;
import com.ssafy.server.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTInterceptor implements HandlerInterceptor {

    private static final String HEADER_AUTH = "Authorization"; // Authorization 헤더 이름
    private static final String BEARER_PREFIX = "Bearer ";      // Bearer 토큰 접두사
    
    private final JWTUtil jwtUtil;

    public JWTInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 요청 처리 전 JWT 토큰 검증 및 유효성 확인
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        String uri = request.getRequestURI();

        if (isSwaggerPath(uri)) {
            return true; // Swagger 경로는 예외 처리
        }

        String token = getTokenFromHeader(request);

        if (!jwtUtil.checkToken(token)) {
            throw new UnAuthorizedException("토큰 검증에 실패했습니다.");
        }

        String userId = jwtUtil.getUserId(token);
        request.setAttribute("userId", userId); // 요청 속성에 userId 저장

        return true;
    }

    /**
     * Swagger 관련 경로인지 확인
     *
     * @param uri 요청 URI
     * @return Swagger 경로 여부
     */
    private boolean isSwaggerPath(String uri) {
        return uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs");
    }

    /**
     * HTTP 요청 헤더에서 Authorization 토큰 추출
     *
     * @param request HTTP 요청 객체
     * @return Bearer 토큰 값
     * @throws UnAuthorizedException 유효하지 않은 토큰 예외
     */
    private String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(HEADER_AUTH);

        if (token == null || !token.startsWith(BEARER_PREFIX)) {
            throw new UnAuthorizedException("유효하지 않은 토큰입니다.");
        }

        return token.substring(BEARER_PREFIX.length()); // "Bearer " 제거 후 반환
    }
}
