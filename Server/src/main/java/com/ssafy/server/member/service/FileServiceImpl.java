package com.ssafy.server.member.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.server.member.dto.FileInfoDto;
import com.ssafy.server.member.mapper.FileMapper;
import com.ssafy.server.member.mapper.MemberMapper;
import com.ssafy.server.util.FileStorageUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
    private final FileStorageUtil fileStorageUtil;
    private final FileMapper fileMapper;
    private final MemberMapper memberMapper;

    public void uploadFile(String userId, MultipartFile file) throws Exception {
        // 파일 저장
        String filePath = fileStorageUtil.storeFile(file);

        // 파일 정보 저장
        FileInfoDto fileInfo = new FileInfoDto(0, file.getOriginalFilename(), filePath);
        fileMapper.uploadFile(fileInfo);

        // 저장 후 생성된 file_id 가져오기
        int fileId = fileInfo.getFileId();

        // 사용자 테이블에 프로필 이미지 업데이트
        memberMapper.updateProfileImage(userId, fileId);
    }

    public FileInfoDto getProfileImage(String userId) throws Exception {
        return fileMapper.getFileInfoByUserId(userId);
    }
}
