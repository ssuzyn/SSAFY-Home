package com.ssafy.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.server.interceptor.JWTInterceptor;

@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    private JWTInterceptor jwtInterceptor;

    public WebConfiguration(JWTInterceptor jwtInterceptor) {
        super();
        this.jwtInterceptor = jwtInterceptor;
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
////		default 설정.
////		Allow all origins.
////		Allow "simple" methods GET, HEAD and POST.
////		Allow all headers.
////		Set max age to 1800 seconds (30 minutes).
//        registry
//            .addMapping("/**")
////            .allowedOrigins("*")
//			.allowedOrigins("http://localhost:5173", "http://localhost:5174","http://127.0.0.1:5174","http://127.0.0.1:5173")
//            .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
//                HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(),
//                HttpMethod.PATCH.name())
//			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
//			.allowCredentials(true)
//			.allowedHeaders("*")
//            .maxAge(1800); // Pre-flight Caching
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5174") // 허용할 출처 명시
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD") // 허용할 메서드 명시
                .allowedHeaders("*") // 허용할 헤더
                .exposedHeaders("Authorization") // 클라이언트에서 접근 가능한 헤더
                .allowCredentials(true) // 쿠키, 인증 정보 허용
                .maxAge(3600); // Preflight 요청 캐시 시간
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 모든 경로에 적용
                .excludePathPatterns( // 해당 경로 제외
                    "/user/login", 
                    "/user/register", 
                    "/swagger-ui/**", // Swagger UI 경로
                    "/v3/api-docs/**", // API Docs 경로
                    "/v3/api-docs", // 추가적인 API Docs 경로
                    "/map/**",
                    "/house/**"
                );
    }


    //	Swagger UI 실행시 404처리
    //	Swagger2 일경우
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/assets/img/");
        registry.addResourceHandler("/*.html**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("classpath:/static/uploads/")
            .setCachePeriod(3600)
            .resourceChain(true);
    }

}

