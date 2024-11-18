package com.ssafy.server.house.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.server.house.dto.HouseDealResponseDto;
import com.ssafy.server.house.mapper.HouseMapper;

@Service
public class HouseServiceImpl implements HouseService{
	
	private final HouseMapper houseMapper;
	
	private HouseServiceImpl(HouseMapper houseMapper) {
		this.houseMapper = houseMapper;
	}

	@Override
	public List<HouseDealResponseDto> getDealsByDong(String dongCode) {
		return houseMapper.getDealsByDong(dongCode);
	}

	@Override
	public List<HouseDealResponseDto> getDealsByAptName(String aptName) {
		return houseMapper.getDealsByAptName(aptName);
	}
	
	@Override
	public List<HouseDealResponseDto> getDealsByDongAndAptName(String dongCode, String aptName) {
		return houseMapper.getDealsByDongAndAptName(dongCode, aptName);
	}

	@Override
	public String getAddress(String dongCode) {
		return houseMapper.getAddress(dongCode);
	}

	
}
