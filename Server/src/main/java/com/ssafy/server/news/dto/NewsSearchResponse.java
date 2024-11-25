package com.ssafy.server.news.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsSearchResponse {
    private List<NewsDto> news;
    private String searchQuery;
    private String searchTimestamp;
}
