package com.ssafy.server.member.service;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.server.member.dto.FileInfoDto;

public interface FileService {
	void uploadFile(String userId, MultipartFile file) throws Exception;
	FileInfoDto getProfileImage(String userId) throws Exception;
}
