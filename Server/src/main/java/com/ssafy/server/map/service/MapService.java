package com.ssafy.server.map.service;

import java.util.List;

import com.ssafy.server.map.dto.SidoGugunCodeDto;


public interface MapService {

	List<SidoGugunCodeDto> getSido() throws Exception;
	List<SidoGugunCodeDto> getGugunInSido(String sido) throws Exception;
	
}
