package com.ssafy.server.house.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "DongCodeDto : 동 코드 정보", description = "시도, 군구, 동에 대한 정보")
public class DongCodeDto {
	
	@Schema(description = "동 코드")
    private String dongCode;      // 동 코드
	
	@Schema(description = "시도 이름")
    private String sidoName;      // 시도 이름
	
	@Schema(description = "구군 이름")
    private String gugunName;     // 군구 이름
	
	@Schema(description = "동 이름")
    private String dongName;      // 동 이름

}
