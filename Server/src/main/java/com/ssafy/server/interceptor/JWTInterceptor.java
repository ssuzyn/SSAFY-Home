package com.ssafy.server.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ssafy.server.exception.UnAuthorizedException;
import com.ssafy.server.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Enumeration;

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
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//
//        String uri = request.getRequestURI();
//
//        if (isSwaggerPath(uri)) {
//            return true; // Swagger 경로는 예외 처리
//        }
//
//        String token = getTokenFromHeader(request);
//
//        if (!jwtUtil.checkToken(token)) {
//            throw new UnAuthorizedException("토큰 검증에 실패했습니다.");
//        }
//
//        String userId = jwtUtil.getUserId(token);
//        request.setAttribute("userId", userId); // 요청 속성에 userId 저장
//
//        return true;
//    }
//
//    /**
//     * Swagger 관련 경로인지 확인
//     *
//     * @param uri 요청 URI
//     * @return Swagger 경로 여부
//     */
//    private boolean isSwaggerPath(String uri) {
//        return uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs");
//    }
//
//    /**
//     * HTTP 요청 헤더에서 Authorization 토큰 추출
//     *
//     * @param request HTTP 요청 객체
//     * @return Bearer 토큰 값
//     * @throws UnAuthorizedException 유효하지 않은 토큰 예외
//     */
//    private String getTokenFromHeader(HttpServletRequest request) {
//        String token = request.getHeader(HEADER_AUTH);
//
//        if (token == null || !token.startsWith(BEARER_PREFIX)) {
//            throw new UnAuthorizedException("유효하지 않은 토큰입니다.");
//        }
//
//        return token.substring(BEARER_PREFIX.length()); // "Bearer " 제거 후 반환
//    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if(CorsUtils.isPreFlightRequest(request)){
            return true;
        }
//        if(request.getMethod().equals("OPTIONS")) return true;

        // 요청 헤더 전체를 로깅
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.debug("Header '{}': {}", headerName, request.getHeader(headerName));
        }

        // Authorization 헤더 확인
        String authHeader = request.getHeader(HEADER_AUTH);
        log.debug("Authorization header: {}", authHeader);

        try {
            String token = getTokenFromHeader(request);
            log.debug("Extracted token: {}", token);

            if (!jwtUtil.checkToken(token)) {
                log.debug("Token validation failed");
                throw new UnAuthorizedException("토큰 검증에 실패했습니다.");
            }

            String userId = jwtUtil.getUserId(token);
            log.debug("Extracted userId: {}", userId);
            request.setAttribute("userId", userId);
            return true;
        } catch (UnAuthorizedException e) {
            log.error("JWT 인증 실패: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\":\"" + e.getMessage() + "\"}");
            return false;
        }
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(HEADER_AUTH);
        log.debug("Raw Authorization header: {}", token);

        if (token == null) {
            throw new UnAuthorizedException("Authorization 헤더가 없습니다.");
        }

        if (!token.startsWith(BEARER_PREFIX)) {
            throw new UnAuthorizedException("Bearer 토큰 형식이 아닙니다. 현재 헤더: " + token);
        }

        return token.substring(BEARER_PREFIX.length()).trim();
    }
}
