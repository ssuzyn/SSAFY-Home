package com.ssafy.server.house.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ApartmentInfoDto {
    private String aptSeq;
    private String aptNm;
    private String dongNm;
    private String latitude;
    private String longitude;
    private List<DealInfoDto> deals;

}

