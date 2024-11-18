package com.ssafy.server.map.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.server.map.dto.SidoGugunCodeDto;
import com.ssafy.server.map.mapper.MapMapper;


@Service
public class MapServiceImpl implements MapService {
	
	private final MapMapper mapMapper;

	public MapServiceImpl(MapMapper mapMapper) {
		super();
		this.mapMapper = mapMapper;
	}

	@Override
	public List<SidoGugunCodeDto> getSido() throws Exception {
		return mapMapper.getSido();
	}

	@Override
	public List<SidoGugunCodeDto> getGugunInSido(String sido) throws Exception {
		return mapMapper.getGugunInSido(sido);
	}

}
