package com.ssafy.server.news.mapper;

import com.ssafy.server.news.dto.NewsDto;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface NewsMapper {
    void insertNews(NewsDto news);
    List<NewsDto> selectNews();
    int checkDuplicateNews(String title, LocalDate date);
    int checkDuplicateNewsByUrl(String link);
}
