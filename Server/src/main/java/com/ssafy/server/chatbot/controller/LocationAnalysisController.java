package com.ssafy.server.chatbot.controller;

import com.ssafy.server.chatbot.dto.LocationAnalysisRequest;
import com.ssafy.server.chatbot.dto.LocationAnalysisResponse;
import com.ssafy.server.chatbot.service.LocationAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Location Analysis", description = "부동산 입지 분석 API")
@RestController
@RequestMapping("/analyze")
@RequiredArgsConstructor
public class LocationAnalysisController {

    private final LocationAnalysisService locationAnalysisService;

    @PostMapping()
    public ResponseEntity<LocationAnalysisResponse> analyzeLocation(@RequestBody LocationAnalysisRequest request) {
        LocationAnalysisResponse response = locationAnalysisService.analyzeLocation(request);
        return ResponseEntity.ok(response);
    }
}
