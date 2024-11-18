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
@Schema(title = "HouseDealRequestDto : 아파트 실거래가 요청 정보", description = "")
public class HouseDealRequestDto {

	@Schema(description = "동 코드")
	private String dongCode;
	
	@Schema(description = "아파트 이름")
	private String aptNm;
}
