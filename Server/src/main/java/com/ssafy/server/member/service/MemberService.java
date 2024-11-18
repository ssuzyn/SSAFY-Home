package com.ssafy.server.member.service;


import com.ssafy.server.member.dto.LoginRequestDto;
import com.ssafy.server.member.dto.MemberInfoDto;

public interface MemberService {

	MemberInfoDto login(LoginRequestDto memberDto) throws Exception;
	void register(MemberInfoDto memberDto) throws Exception;
	MemberInfoDto userInfo(String userId) throws Exception;
	void updateUserInfo(MemberInfoDto memberDto) throws Exception;
	
	void saveRefreshToken(String userId, String refreshToken) throws Exception;
	Object getRefreshToken(String userId) throws Exception;
	void deleRefreshToken(String userId) throws Exception;
	
}
