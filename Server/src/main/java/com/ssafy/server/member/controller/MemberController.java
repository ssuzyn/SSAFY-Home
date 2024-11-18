package com.ssafy.server.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.server.member.dto.LoginRequestDto;
import com.ssafy.server.member.dto.MemberInfoDto;
import com.ssafy.server.member.service.FileService;
import com.ssafy.server.member.service.MemberService;
import com.ssafy.server.util.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "회원 인증 컨트롤러", description = "로그인 로그아웃, 회원 정보 처리하는 클래스.")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final FileService fileService;
    private final JWTUtil jwtUtil;

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 이용하여 로그인 처리.")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
        @RequestBody @Parameter(description = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) LoginRequestDto memberDto) {
        log.debug("login user : {}", memberDto);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
        	MemberInfoDto loginUser = memberService.login(memberDto);
            if(loginUser != null) {
                String accessToken = jwtUtil.createAccessToken(loginUser.getUserId());
                String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserId());
                log.debug("access token : {}", accessToken);
                log.debug("refresh token : {}", refreshToken);

//				발급받은 refresh token 을 DB에 저장.
                memberService.saveRefreshToken(loginUser.getUserId(), refreshToken);

//				JSON 으로 token 전달.
                resultMap.put("access-token", accessToken);
                resultMap.put("refresh-token", refreshToken);

                status = HttpStatus.CREATED;
            } else {
                resultMap.put("message", "아이디 또는 패스워드를 확인해 주세요.");
                status = HttpStatus.UNAUTHORIZED;
            }

        } catch (Exception e) {
            log.debug("로그인 에러 발생 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @Operation(summary = "회원가입", description = "회원가입을 처리한다.")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
        @RequestBody @Parameter(description = "회원가입에 필요한 회원정보", required = true) MemberInfoDto memberDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            memberService.register(memberDto);
            resultMap.put("message", "회원가입 성공!");
            status = HttpStatus.CREATED;
        } catch (Exception e) {
            log.error("회원가입 실패 : {}", e.getMessage(), e);
            resultMap.put("message", "회원가입 중 문제가 발생했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @Operation(summary = "회원 정보 조회", description = "요청에 포함된 사용자 정보를 반환합니다.")
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getMemberInfo(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            String userId = (String) request.getAttribute("userId");
            log.debug("추출된 userId: {}", userId);

            MemberInfoDto memberDto = memberService.userInfo(userId);
            resultMap.put("message", "회원 정보 조회 성공");
            resultMap.put("userInfo", memberDto);
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("회원 정보 조회 중 에러 발생: {}", e.getMessage(), e);
            resultMap.put("message", "회원 정보 조회 중 문제가 발생했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @Operation(summary = "회원 정보 수정", description = "token으로 요청해서 받은 회원 정보를 바탕으로 수정 요청 DTO 채워서 보내야 합니다.")
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUserInfo(
        HttpServletRequest request,
        @RequestBody @Parameter(description = "수정할 회원 정보", required = true) MemberInfoDto memberDto) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            // JWTInterceptor에서 저장된 userId를 가져옴
            String userId = (String) request.getAttribute("userId");
            log.debug("추출된 userId: {}", userId);

            memberDto.setUserId(userId);

            // 회원 정보 업데이트
            memberService.updateUserInfo(memberDto);

            resultMap.put("message", "회원 정보가 성공적으로 수정되었습니다.");
            status = HttpStatus.OK;

        } catch (Exception e) {
            log.error("회원 정보 수정 중 에러 발생: {}", e.getMessage(), e);
            resultMap.put("message", "회원 정보 수정 중 문제가 발생했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
    
    @PostMapping(value= "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
    	    summary = "회원 사진 업로드",
    	    description = "회원 사진 파일을 업로드합니다."
    	)
    public ResponseEntity<Map<String, Object>> uploadFile(
        HttpServletRequest request,
        @RequestParam("file") @Parameter(description = "업로드할 프로필 사진 파일", required = true, 
                content = @Content(mediaType = "multipart/form-data")) MultipartFile file) {
        
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            String userId = (String) request.getAttribute("userId");
            fileService.uploadFile(userId, file);

            resultMap.put("message", "파일 업로드 성공");
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage(), e);
            resultMap.put("message", "파일 업로드 중 문제가 발생했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }


    @Operation(summary = "로그아웃", description = "사용자 정보를 기반으로 로그아웃합니다.")
    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            String userId = (String) request.getAttribute("userId");
            log.debug("로그아웃 요청 userId: {}", userId);

            memberService.deleRefreshToken(userId);
            resultMap.put("message", "로그아웃 성공");
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("로그아웃 실패 : {}", e.getMessage(), e);
            resultMap.put("message", "로그아웃 중 문제가 발생했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
