package com.ssafy.server.map.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.server.map.dto.DongCodeDto;
import com.ssafy.server.map.dto.SidoGugunCodeDto;
import com.ssafy.server.map.service.MapService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/map")
@Tag(name = "시도 구군 컨트롤러", description = "시도 구군정보를 처리하는 클래스.")
@Slf4j
public class MapController {
	
	private final MapService mapService;

	public MapController(MapService mapService) {
		super();
		this.mapService = mapService;
	}

	@Operation(summary = "시도 정보", description = "전국의 시도를 반환한다.")
	@GetMapping("/sido")
	public ResponseEntity<List<SidoGugunCodeDto>> getSido() throws Exception {
		log.info("sido - 호출");
		return new ResponseEntity<List<SidoGugunCodeDto>>(mapService.getSido(), HttpStatus.OK);
	}

	@Operation(summary = "구군 정보", description = "시도에 속한 구군을 반환한다.")
	@GetMapping("/gugun")
	public ResponseEntity<List<SidoGugunCodeDto>> getGugun(
			@RequestParam("sido") @Parameter(description = "시도코드.", required = true) String sido) throws Exception {
		log.info("gugun - 호출");
		return new ResponseEntity<List<SidoGugunCodeDto>>(mapService.getGugunInSido(sido), HttpStatus.OK);
	}
	
	@Operation(summary = "동 정보", description = "구군에 속한 동을 반환한다.")
	@GetMapping("/dong")
	public ResponseEntity<List<DongCodeDto>> getDong(
			@RequestParam("gugun") @Parameter(description = "구군코드.", required = true) String gugun) throws Exception {
		log.info("gugun - 호출");
		return new ResponseEntity<List<DongCodeDto>>(mapService.getDongInGugun(gugun), HttpStatus.OK);
	}
}
