package com.ssafy.server.house.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.server.house.dto.ApartmentInfoDto;
import com.ssafy.server.house.dto.HouseDealRequestDto;
import com.ssafy.server.house.dto.HouseDealResponseDto;
import com.ssafy.server.house.service.HouseService;

import io.swagger.v3.oas.annotations.Operation;
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
    
    @GetMapping("/get/{aptSeq}")
    @Operation(summary = "특정 아파트 거래 정보 조회", description = "aptSeq를 기준으로 아파트 기본 정보와 실거래 데이터를 반환합니다.")
    public ResponseEntity<ApartmentInfoDto> getDealByAptNm(@PathVariable String aptSeq) {
        log.info("아파트 거래 정보 조회 요청: aptSeq={}", aptSeq);

        // 서비스 계층에서 아파트 정보와 거래 정보 조회
        ApartmentInfoDto apartmentInfo = houseService.getApartmentInfo(aptSeq);

        if (apartmentInfo == null) {
            log.warn("아파트 정보를 찾을 수 없음: aptSeq={}", aptSeq);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        log.info("아파트 정보 반환 성공: {}", apartmentInfo);
        return ResponseEntity.ok(apartmentInfo);
    }


    @PostMapping("/getDeals")
    public ResponseEntity<Map<String, Object>> getDeals(@RequestBody HouseDealRequestDto houseDealRequestDto) {
    	
    	String dongCode = houseDealRequestDto.getDongCode();
    	String aptNm = houseDealRequestDto.getAptNm();
    	
        try {
            if (dongCode == null && aptNm == null) {
                return createErrorResponse("동코드나 아파트 이름 중 하나를 입력해야 합니다.", HttpStatus.BAD_REQUEST);
            }
            
            List<HouseDealResponseDto> houseDealDto = houseService.getDeals(dongCode, aptNm);
            
            if (houseDealDto.isEmpty()) {
                return createResponse("해당 조건에 대한 거래 정보가 없습니다.", Collections.emptyList());
            }

            return createResponse("거래 정보 조회 성공", houseDealDto);

        } catch (Exception e) {
            log.error("거래 정보 조회 중 에러 발생: {}", e.getMessage(), e);
            return createErrorResponse("거래 정보를 불러오는 중 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
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
