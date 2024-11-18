package com.ssafy.server.member.service;


import com.ssafy.server.member.dto.MemberDto;

public interface MemberService {

	MemberDto login(MemberDto memberDto) throws Exception;
	void register(MemberDto memberDto) throws Exception;
	MemberDto userInfo(String userId) throws Exception;
	void saveRefreshToken(String userId, String refreshToken) throws Exception;
	Object getRefreshToken(String userId) throws Exception;
	void deleRefreshToken(String userId) throws Exception;
	
}
