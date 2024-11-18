package com.ssafy.server.map.mapper;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.server.map.dto.SidoGugunCodeDto;


public interface MapMapper {
	List<SidoGugunCodeDto> getSido() throws SQLException;
	List<SidoGugunCodeDto> getGugunInSido(String sido) throws SQLException;
}
