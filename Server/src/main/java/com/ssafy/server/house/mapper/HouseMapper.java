package com.ssafy.server.house.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.server.house.dto.HouseDealResponseDto;

@Mapper
public interface HouseMapper {

    List<HouseDealResponseDto> getDealsByDong(String dongCode);
    List<HouseDealResponseDto> getDealsByAptName(String aptName);
    List<HouseDealResponseDto> getDealsByDongAndAptName(String dongCode, String aptName);
    String getAddress(String dongCode);
}
