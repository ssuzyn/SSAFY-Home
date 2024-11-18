package com.ssafy.server.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileInfoDto {
    private int fileId;               // 파일 ID (DB에서 자동 증가)
    private String originalName;  // 원본 파일명
    private String savePath;      // 저장 경로
}
