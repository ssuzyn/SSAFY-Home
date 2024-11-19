package com.ssafy.server.map.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "DongCodeDto : 동 코드 정보", description = "동에 대한 정보")
public class DongCodeDto {
	
	@Schema(description = "동 코드")
    private String dongCode;      // 동 코드
	
	@Schema(description = "동 이름")
    private String dongName;      // 동 이름

}
