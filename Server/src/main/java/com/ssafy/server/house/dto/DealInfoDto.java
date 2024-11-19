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
@Schema(title = "DealInfoDto : 아파트 상세 정보", description = "특정 아파트의 상세 정보와 거래 데이터를 담는 DTO")
public class DealInfoDto {
	private String floor;
    private double excluUseAr;
    private String dealAmount;
    private String dealDate;
}
