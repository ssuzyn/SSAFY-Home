package com.ssafy.server.house.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.server.house.dto.ApartmentInfoDto;
import com.ssafy.server.house.dto.DealInfoDto;
import com.ssafy.server.house.dto.HouseDealResponseDto;

@Mapper
public interface HouseMapper {

    List<HouseDealResponseDto> getDeals(String dongCode, String aptName);
    String getAddress(String dongCode);
    
    ApartmentInfoDto getApartmentInfo(String aptSeq);
    List<DealInfoDto> getDealInfo(String aptSeq);
    
}
