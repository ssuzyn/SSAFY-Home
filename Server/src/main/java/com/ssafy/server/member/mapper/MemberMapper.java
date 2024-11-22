package com.ssafy.server.member.mapper;


import com.ssafy.server.member.dto.UpdateRequestDto;
import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.server.member.dto.LoginRequestDto;
import com.ssafy.server.member.dto.MemberInfoDto;

@Mapper
public interface MemberMapper {

	MemberInfoDto login(LoginRequestDto memberDto) throws SQLException;
    void register(MemberInfoDto memberDto) throws SQLException;
    MemberInfoDto findByUserId(String userId) throws SQLException;
	void updateUserInfo(UpdateRequestDto memberDto) throws SQLException;
    void deleteByUserId(String userId) throws SQLException;
	
    void saveRefreshToken(Map<String, String> map) throws SQLException;
    Object getRefreshToken(String userid) throws SQLException;
    void deleteRefreshToken(Map<String, String> map) throws SQLException;
    int checkIdAvailability(String userId) throws SQLException;
}

