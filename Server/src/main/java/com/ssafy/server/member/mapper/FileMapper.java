package com.ssafy.server.member.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.server.member.dto.FileInfoDto;

@Mapper
public interface FileMapper {

    void uploadFile(FileInfoDto fileInfo) throws SQLException;
    FileInfoDto getFileInfoByUserId(String userId) throws SQLException;
}
