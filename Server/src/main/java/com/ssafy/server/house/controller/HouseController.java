package com.ssafy.server.house.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.server.house.dto.HouseDealRequestDto;
import com.ssafy.server.house.dto.HouseDealResponseDto;
import com.ssafy.server.house.service.HouseService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/house")
@Tag(name = "아파트 거래 컨트롤러", description = "아파트 거래 정보를 처리하는 클래스.")
@Slf4j
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @PostMapping("/getDeals")
    public ResponseEntity<Map<String, Object>> getDeals(@RequestBody HouseDealRequestDto houseDealRequestDto) {
    	String dongCode = houseDealRequestDto.getDongCode();
    	String aptName = houseDealRequestDto.getAptNm();
    	
        try {
            if (dongCode == null && aptName == null) {
                return createErrorResponse("동코드나 아파트 이름 중 하나를 입력해야 합니다.", HttpStatus.BAD_REQUEST);
            }

            List<HouseDealResponseDto> houseDealDto = getHouseDeals(dongCode, aptName);
            if (houseDealDto == null || houseDealDto.isEmpty()) {
                return createResponse("해당 조건에 대한 거래 정보가 없습니다.", Collections.emptyList());
            }

            log.debug("Number of deals found: {}", houseDealDto.size());
            return createResponse("거래 정보 조회 성공", houseDealDto);

        } catch (Exception e) {
            log.error("거래 정보 조회 중 에러 발생: {}", e.getMessage(), e);
            return createErrorResponse("거래 정보를 불러오는 중 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<HouseDealResponseDto> getHouseDeals(String dongCode, String aptName) {
        if (dongCode != null && aptName != null) {
            log.debug("dongCode: {}, aptName: {}", dongCode, aptName);
            return houseService.getDealsByDongAndAptName(dongCode, aptName);
        } else if (dongCode != null) {
            log.debug("dongCode: {}", dongCode);
            return houseService.getDealsByDong(dongCode);
        } else {
            log.debug("aptName: {}", aptName);
            return houseService.getDealsByAptName(aptName);
        }
    }

    private ResponseEntity<Map<String, Object>> createResponse(String message, Object data) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("message", message);
        resultMap.put("data", data);
        return ResponseEntity.ok(resultMap);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("message", message);
        return ResponseEntity.status(status).body(resultMap);
    }
}
