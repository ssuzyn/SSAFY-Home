package com.ssafy.server.news.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class NewsDto {
    private Long id;           // 뉴스 ID
    private String title;      // 뉴스 제목
    private String link;       // 뉴스 링크
    private LocalDate date;    // 뉴스 날짜
}
