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
@Schema(title = "HouseDealResponseDto", description = "최신 거래 내역을 나타내는 DTO")
public class HouseDealResponseDto {
    private String aptNm;
    private String floor;
    private double excluUseAr;
    private String dealAmount;
    private int dealYear;
    private int dealMonth;
    private int dealDay;
    private String latitude;
    private String longitude;
}
