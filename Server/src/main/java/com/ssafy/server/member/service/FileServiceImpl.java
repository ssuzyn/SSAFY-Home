package com.ssafy.server.member.service;

import com.ssafy.server.member.dto.FileInfoDto;
import com.ssafy.server.member.mapper.FileMapper;
import com.ssafy.server.util.FileStorageUtil;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {
    private final FileMapper fileMapper;
    private final FileStorageUtil fileStorageUtil;

    @Override
    public void uploadFile(String userId, MultipartFile file) throws Exception {
        FileInfoDto existingFile = fileMapper.getFileInfoByUserId(userId);
        if (existingFile != null) {
            log.info("기존 파일 삭제 시도: {}", existingFile.getSavePath());
            deleteProfileImage(existingFile);
        } else {
            log.info("기존 파일이 없어 삭제하지 않습니다.");
        }

        String relativePath = fileStorageUtil.storeFile(userId, file);
        FileInfoDto newFile = new FileInfoDto(userId, file.getOriginalFilename(), relativePath);
        fileMapper.uploadFile(newFile);
        log.info("새 파일 업로드 및 DB 저장 완료: {}", relativePath);
    }

    @Override
    public void setDefaultProfileImage(String userId) throws Exception {
        FileInfoDto existingFile = fileMapper.getFileInfoByUserId(userId);
        if (existingFile != null) {
            log.info("기존 파일 삭제 시도: {}", existingFile.getSavePath());
            deleteProfileImage(existingFile);
        } else {
            log.info("기존 파일이 없어 삭제하지 않습니다.");
        }

        String relativePath = fileStorageUtil.copyDefaultProfile(userId);
        FileInfoDto defaultProfile = new FileInfoDto(userId, "basic_profile.png", relativePath);
        fileMapper.uploadFile(defaultProfile);
        log.info("기본 프로필 업로드 및 DB 저장 완료: {}", relativePath);
    }


    @Override
    public FileInfoDto getProfileImage(String userId) throws Exception {
        return fileMapper.getFileInfoByUserId(userId);
    }

    @Override
    public void deleteFilesByUserId(String userId) throws Exception {
        FileInfoDto file = fileMapper.getFileInfoByUserId(userId);
        if (file != null) {
            deleteProfileImage(file);
        }
    }

    private void deleteProfileImage(FileInfoDto file) throws Exception {
        // file.upload-dir 설정에 따라 절대 경로 생성
        Path uploadRootPath = Paths.get(fileStorageUtil.getUploadDir()).toAbsolutePath().normalize();

        // file.getSavePath()는 userId/파일명 형태이므로 조합
        Path absolutePath = uploadRootPath.resolve(file.getSavePath()).normalize();

        File fileOnDisk = absolutePath.toFile();

        // 파일 존재 여부 확인 및 삭제
        if (fileOnDisk.exists()) {
            if (fileOnDisk.delete()) {
                log.info("파일 삭제 성공: {}", absolutePath);
            } else {
                throw new RuntimeException("파일 삭제 실패: " + absolutePath);
            }
        } else {
            log.warn("파일이 존재하지 않아 삭제를 생략합니다: {}", absolutePath);
        }

        // DB에서 파일 정보 삭제
        fileMapper.deleteFileByUserId(file.getUserId());
    }


}