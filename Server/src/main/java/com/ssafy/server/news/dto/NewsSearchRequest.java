package com.ssafy.server.news.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsSearchRequest {
    private String query;
    private int numResults = 5;
}