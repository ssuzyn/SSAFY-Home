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
@Schema(title = "HouseDealDto : 아파트 거래 정보", description = "아파트 거래 정보를 나타낸다.")
public class HouseDealDto {
	
	private int no;                  // 일련번호
    private String aptSeq;           // 아파트 코드
    private String aptDong;          // 아파트 동수
    private String floor;            // 층수
    private int dealYear;            // 거래 년도
    private int dealMonth;           // 거래 월
    private int dealDay;             // 거래 일
    private double excluUseAr;       // 전용면적
    private String dealAmount;       // 거래 금액

}
