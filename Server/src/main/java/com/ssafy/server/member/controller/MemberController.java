package com.ssafy.server.member.controller;

import com.ssafy.server.member.dto.UpdateRequestDto;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
//                System.out.println(accessToken);

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
            fileService.setDefaultProfileImage(memberDto.getUserId());
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

    @Operation(summary = "회원 정보 수정", description = "프로필 사진을 제외한 요청에 포함된 사용자 정보를 수정합니다.")
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUserInfo(
        HttpServletRequest request,
        @RequestBody UpdateRequestDto updateDto) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            String userId = (String) request.getAttribute("userId");

            updateDto.setUserId(userId);

            memberService.updateUserInfo(updateDto);

            resultMap.put("message", "회원 정보가 성공적으로 수정되었습니다.");
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("회원 정보 수정 중 에러 발생", e);
            resultMap.put("message", "회원 정보 수정 중 문제가 발생했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @Operation(summary = "회원 프로필 사진 업로드", description = "프로필 사진을 업로드합니다.")
    @PostMapping(value = "/uploadProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadProfileImage(
        HttpServletRequest request,
        @RequestParam("file") MultipartFile file) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            String userId = (String) request.getAttribute("userId");

            if (file != null && !file.isEmpty()) {
                fileService.uploadFile(userId, file);
                resultMap.put("message", "프로필 사진이 성공적으로 업로드되었습니다.");
                status = HttpStatus.OK;
            } else {
                resultMap.put("message", "파일이 비어 있습니다.");
                status = HttpStatus.BAD_REQUEST;
            }
        } catch (Exception e) {
            log.error("프로필 사진 업로드 중 에러 발생", e);
            resultMap.put("message", "프로필 사진 업로드 중 문제가 발생했습니다.");
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

    @DeleteMapping("/deleteProfile")
    @Operation(summary = "프로필 사진 삭제", description = "회원의 프로필 사진을 삭제하고 기본 프로필로 변경합니다.")
    public ResponseEntity<Map<String, Object>> deleteProfile(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            String userId = (String) request.getAttribute("userId");
            log.debug("프로필 삭제 요청 userId: {}", userId);

            // 기본 프로필 이미지 설정
            fileService.setDefaultProfileImage(userId);

            resultMap.put("message", "프로필 사진이 기본 프로필로 변경되었습니다.");
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("프로필 사진 삭제 중 오류 발생: {}", e.getMessage(), e);
            resultMap.put("message", "프로필 사진 삭제 중 문제가 발생했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @Operation(summary = "회원 탈퇴", description = "사용자 정보를 기반으로 회원 탈퇴를 처리합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteUser(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            // JWTInterceptor에서 추출한 userId 가져오기
            String userId = (String) request.getAttribute("userId");
            log.debug("회원 탈퇴 요청 userId: {}", userId);

            // 회원 탈퇴 처리
            memberService.deleteUser(userId);

            resultMap.put("message", "회원 탈퇴가 성공적으로 처리되었습니다.");
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("회원 탈퇴 중 에러 발생: {}", e.getMessage(), e);
            resultMap.put("message", "회원 탈퇴 처리 중 문제가 발생했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
