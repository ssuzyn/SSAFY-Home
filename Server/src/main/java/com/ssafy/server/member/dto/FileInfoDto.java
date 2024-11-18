package com.ssafy.server.member.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class FileInfoDto {
    private Integer fileId;
    private String userId;
    private String originalName;  // 원본 파일명
    private String savePath;      // 저장 경로

    public FileInfoDto(String userId, String originalName, String savePath) {
        this.userId = userId;
        this.originalName = originalName;
        this.savePath = savePath;
    }
}
