package com.ssafy.server.interest.controller;

import com.ssafy.server.interest.dto.InterestAptDto;
import com.ssafy.server.interest.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("interest-apt")
@Tag(name = "관심 매물 관리 컨트롤러", description = "사용자의 관심 매물을 관리하는 클래스.")
@Slf4j
public class InterestController {

    private final InterestService interestService;

    @GetMapping("/list")
    @Operation(summary = "관심 매물 조회", description = "사용자의 관심 매물 리스트 조회")
    public ResponseEntity<Map<String, Object>> getInterestList(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String userId = (String) request.getAttribute("userId");
            if (userId == null) {
                response.put("message", "인증 정보가 올바르지 않습니다.");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            List<InterestAptDto> list = interestService.getInterestApts(userId);
            log.info("관심 매물 조회 결과 개수: {}", list.size());

            response.put("message", "관심 매물 목록 조회가 완료되었습니다.");
            response.put("data", list);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("관심 매물 조회 중 오류 발생", e);
            response.put("message", "서버 오류가 발생했습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add/{aptSeq}")
    @Operation(summary = "관심 매물 추가", description = "사용자의 관심 매물 추가")
    public ResponseEntity<Map<String, Object>> addInterest(HttpServletRequest request, @PathVariable String aptSeq) {
        Map<String, Object> response = new HashMap<>();
        try {
            String userId = (String) request.getAttribute("userId");
            if (userId == null) {
                response.put("message", "인증 정보가 올바르지 않습니다.");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            interestService.addInterest(userId, aptSeq);
            log.info("관심 매물이 성공적으로 등록되었습니다. userId: {}, aptSeq: {}", userId, aptSeq);

            response.put("message", "관심 매물 등록이 완료되었습니다.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalStateException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("관심 매물 추가 중 오류 발생", e);
            response.put("message", "서버 오류가 발생했습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{aptSeq}")
    @Operation(summary = "관심 매물 삭제", description = "사용자의 관심 매물 삭제")
    public ResponseEntity<Map<String, Object>> removeInterest(HttpServletRequest request, @PathVariable String aptSeq) {
        Map<String, Object> response = new HashMap<>();
        try {
            String userId = (String) request.getAttribute("userId");
            if (userId == null) {
                response.put("message", "인증 정보가 올바르지 않습니다.");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            interestService.removeInterest(userId, aptSeq);
            response.put("message", "관심 매물이 삭제되었습니다.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalStateException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("관심 매물 삭제 중 오류 발생", e);
            response.put("message", "서버 오류가 발생했습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}