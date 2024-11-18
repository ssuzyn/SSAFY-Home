package com.ssafy.server.util;

import java.io.IOException;
import java.nio.file.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileStorageUtil {

    @Getter
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 기본 프로필 이미지 복사 메서드
     * @param userId 사용자 ID
     * @return 복사된 파일의 상대 경로
     * @throws IOException 파일 복사 실패 시 예외 발생
     */
    public String copyDefaultProfile(String userId) throws IOException {
        // 기본 프로필 경로
        Path defaultProfilePath = Paths.get(uploadDir, "basic_profile.png");
        if (!Files.exists(defaultProfilePath)) {
            throw new IOException("기본 프로필 이미지가 존재하지 않습니다: " + defaultProfilePath);
        }

        // 사용자 디렉토리 생성
        Path userDirectory = ensureDirectoryExists(userId);

        // 복사된 파일 경로
        Path copiedFilePath = userDirectory.resolve("basic_profile.png");

        // 기존 파일 삭제 후 복사
        Files.deleteIfExists(copiedFilePath);
        Files.copy(defaultProfilePath, copiedFilePath, StandardCopyOption.REPLACE_EXISTING);

        // 반환할 상대 경로
        return Paths.get(userId, "basic_profile.png").toString();
    }

    /**
     * 파일 저장 메서드
     * @param userId 사용자 ID
     * @param file 저장할 파일
     * @return 저장된 파일의 상대 경로
     * @throws IOException 파일 저장 실패 시 예외 발생
     */
    public String storeFile(String userId, MultipartFile file) throws IOException {
        // 사용자 디렉토리 생성
        Path userDirectory = ensureDirectoryExists(userId);

        // 파일 저장 경로 설정
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IOException("파일 이름이 유효하지 않습니다.");
        }
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
        Path filePath = userDirectory.resolve(uniqueFileName);

        // 파일 저장
        file.transferTo(filePath.toFile());
        log.info("파일 저장 완료: {}", filePath);

        // 반환할 상대 경로
        return Paths.get(userId, uniqueFileName).toString();
    }

    /**
     * 디렉토리 확인 및 생성 메서드
     * @param userId 사용자 ID
     * @return 생성되거나 존재하는 디렉토리의 Path 객체
     * @throws IOException 디렉토리 생성 실패 시 예외 발생
     */
    private Path ensureDirectoryExists(String userId) throws IOException {
        Path userDirectory = Paths.get(uploadDir, userId);

        // 디렉토리가 없으면 생성
        if (!Files.exists(userDirectory)) {
            Files.createDirectories(userDirectory);
            log.info("사용자 디렉토리가 생성되었습니다: {}", userDirectory);
        } else {
            log.info("사용자 디렉토리가 이미 존재합니다: {}", userDirectory);
        }

        return userDirectory;
    }
}
