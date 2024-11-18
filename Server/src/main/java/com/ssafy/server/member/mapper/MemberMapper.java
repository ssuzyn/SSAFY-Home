package com.ssafy.server.member.mapper;


import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.server.member.dto.LoginRequestDto;
import com.ssafy.server.member.dto.MemberInfoDto;

@Mapper
public interface MemberMapper {

	MemberInfoDto login(LoginRequestDto memberDto) throws SQLException;
    void register(MemberInfoDto memberDto) throws SQLException;
    MemberInfoDto userInfo(String userId) throws SQLException;
	void updateUserInfo(MemberInfoDto memberDto) throws Exception;
    void saveRefreshToken(Map<String, String> map) throws SQLException;
    Object getRefreshToken(String userid) throws SQLException;
    void deleteRefreshToken(Map<String, String> map) throws SQLException;

}

