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
@Schema(title = "UpdateRequestDto : 수정할 회원 정보", description = "수정할 회원의 정보를 나타낸다.")
public class UpdateRequestDto {
    @Schema(description = "아이디", example = "snoopy")
    private String userId;
    @Schema(description = "이름", example = "스누피")
    private String userName;
    @Schema(description = "비밀번호", example = "1234")
    private String userPwd;
    @Schema(description = "이메일 아이디", example = "snoopy")
    private String emailId;
    @Schema(description = "이메일 도메인", example = "gmail.com")
    private String emailDomain;
    @Schema(description = "성별", example = "F")
    private String gender;
    @Schema(description = "나이", example = "25")
    private int age;
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;
}
