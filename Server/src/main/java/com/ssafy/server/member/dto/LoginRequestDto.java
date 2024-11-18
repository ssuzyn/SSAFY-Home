package com.ssafy.server.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "LoginRequestDto : 로그인 요청 정보", description = "사용자의 로그인 요청 DTO")
public class LoginRequestDto {

	@Schema(description = "아이디", example = "snoopy")
    private String userId;
	@Schema(description = "비밀번호", example = "1234")
    private String userPwd;
}
