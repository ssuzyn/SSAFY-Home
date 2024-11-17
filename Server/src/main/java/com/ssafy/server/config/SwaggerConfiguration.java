package com.ssafy.server.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Swagger-UI 확인
//http://localhost/vue/swagger-ui/index.html

@OpenAPIDefinition(
	    info = @Info(
	        title = "SSAFY Home API 명세서",
	        description = "<h3>SSAFY API Reference for Developers</h3>",
	        version = "v1"
	    )
	)
@Configuration
public class SwaggerConfiguration {
	
//	@Bean
//	public OpenAPI openAPI() {
//		Info info = new Info().title("SSAFY VUE API 명세서").description(
//				"<h3>SSAFY API Reference for Developers</h3>Swagger를 이용한 VUE API<br><img src=\"/assets/img/ssafy_logo.png\" width=\"150\">")
//				.version("v1").contact(new io.swagger.v3.oas.models.info.Contact().name("hissam")
//						.email("hissam@ssafy.com").url("http://edu.ssafy.com"));
//
//		return new OpenAPI().components(new Components()).info(info);
//	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.components(new Components()
				.addSecuritySchemes("BearerAuth",
					new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
				)
			)
			.addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
	}


	@Bean
	public GroupedOpenApi allApi() {
		return GroupedOpenApi.builder().group("ssafy-all").pathsToMatch("/**").build();
	}

	@Bean
	public GroupedOpenApi boardApi() {
		return GroupedOpenApi.builder().group("ssafy-board").pathsToMatch("/board/**").build();
	}
	
	@Bean
	public GroupedOpenApi userApi() {
		return GroupedOpenApi.builder().group("ssafy-user").pathsToMatch("/user/**").build();
	}
	
	@Bean
	public GroupedOpenApi mapApi() {
		return GroupedOpenApi.builder().group("ssafy-map").pathsToMatch("/map/**").build();
	}
	
}
