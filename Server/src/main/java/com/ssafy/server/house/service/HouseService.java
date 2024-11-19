package com.ssafy.server.house.service;

import java.util.List;

import com.ssafy.server.house.dto.ApartmentInfoDto;
import com.ssafy.server.house.dto.HouseDealResponseDto;

public interface HouseService {
	
    List<HouseDealResponseDto> getDealsByDong(String dongCode);
    List<HouseDealResponseDto> getDealsByAptName(String aptName);
    List<HouseDealResponseDto> getDealsByDongAndAptName(String dongCode, String aptName);
    String getAddress(String dongCode);
    
    ApartmentInfoDto getApartmentInfo(String aptSeq);
}
