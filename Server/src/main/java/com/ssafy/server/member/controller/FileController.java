package com.ssafy.server.member.controller;

import com.ssafy.server.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileStorageUtil fileStorageUtil;

    @GetMapping("/image/{*path}")
    public ResponseEntity<Resource> getImage(@PathVariable String path) {
        try {
            Resource resource = new FileSystemResource(fileStorageUtil.getUploadDir() + path);

            if (!resource.exists()) {
                log.error("파일을 찾을 수 없습니다: {}", path);
                return ResponseEntity.notFound().build();
            }

            MediaType mediaType = MediaType.parseMediaType(determineContentType(path));
            return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);

        } catch (Exception e) {
            log.error("이미지 로드 중 에러 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            default -> "application/octet-stream";
        };
    }
}