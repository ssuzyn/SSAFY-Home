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
@Schema(title = "HouseInfoDto", description = "아파트 위치 정보를 나타내는 DTO")
public class HouseInfoDto {

	private String aptSeq;          // 아파트 코드
    private String sggCd;           // 시구군 코드
    private String umdCd;           // 읍면동 코드
    private String umdNm;           // 읍면동 이름
    private String jibun;           // 지번
    private String roadNmSggCd;     // 도로명 시구군 코드
    private String roadNm;          // 도로명
    private String roadNmBonbun;    // 도로명 본번
    private String roadNmBubun;     // 도로명 부번
    private String aptNm;           // 아파트 이름
    private int buildYear;          // 건축년도
    private String latitude;        // 위도
    private String longitude;       // 경도
}
