package com.ssafy.server.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**Swagger-UI 확인
 * http://localhost/swagger-ui/index.html
*/

@OpenAPIDefinition(info = @Info(title = "SSAFY Home API 명세서", description = "<h3>SSAFY API Reference for Developers</h3>", version = "v1"))
@Configuration
public class SwaggerConfiguration {

	@Bean
	public OpenAPI api() {
		SecurityScheme apiKey = new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
				.name("Authorization");

		SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer Token");

		return new OpenAPI().components(new Components().addSecuritySchemes("Bearer Token", apiKey))
				.addSecurityItem(securityRequirement);
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

	@Bean
	public GroupedOpenApi houseApi() {
		return GroupedOpenApi.builder().group("ssafy-house").pathsToMatch("/house/**").build();
	}

}
