package com.ssafy.server.member.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.server.member.dto.FileInfoDto;

public interface FileService {
	// 파일 업로드
	void uploadFile(String userId, MultipartFile file) throws Exception;

	// 사용자 프로필 이미지 가져오기
	FileInfoDto getProfileImage(String userId) throws Exception;

	// 기본 프로필 이미지로 설정
	void setDefaultProfileImage(String userId) throws Exception;

	// 사용자 ID로 파일 삭제
	void deleteFilesByUserId(String userId) throws Exception;
}