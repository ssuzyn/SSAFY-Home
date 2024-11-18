package com.ssafy.server.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileStorageUtil {

	@Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws Exception {
        // 설정된 경로를 기반으로 Path 객체 생성
        Path targetLocation = Paths.get(uploadDir);

        // 파일 이름 생성
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
        Path filePath = targetLocation.resolve(uniqueFileName);

        // 디렉토리 생성
        Files.createDirectories(targetLocation);
        file.transferTo(filePath);

        log.debug("파일 저장 완료: {}", filePath.toString());
        return filePath.toString();
    }
}