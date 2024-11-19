package com.ssafy.server.house.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "ApartmentInfoDto : 아파트 상세 정보", description = "아파트 정보 시각화를 위한 DTO")
public class ApartmentInfoDto {
    private String aptSeq;
    private String aptNm;
    private String dongNm;
    private String latitude;
    private String longitude;
    private List<DealInfoDto> deals;

}

