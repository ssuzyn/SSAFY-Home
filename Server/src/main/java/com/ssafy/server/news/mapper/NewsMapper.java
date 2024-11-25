package com.ssafy.server.news.mapper;

import com.ssafy.server.news.dto.NewsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsMapper {
    void insertNews(NewsDto news);
    List<NewsDto> selectNews();
}
