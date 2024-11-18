package com.ssafy.server.member.mapper;

import java.sql.SQLException;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.ssafy.server.member.dto.FileInfoDto;

@Mapper
public interface FileMapper {

    void uploadFile(FileInfoDto fileInfo) throws SQLException;
    FileInfoDto getFileInfoByUserId(String userId) throws SQLException;
    List<FileInfoDto> getFilesByUserId(String userId);
    void deleteFileByUserId(String userId) throws SQLException;
}
