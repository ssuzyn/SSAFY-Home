package com.ssafy.server.member.service;


import com.ssafy.server.member.dto.LoginRequestDto;
import com.ssafy.server.member.dto.MemberInfoDto;
import com.ssafy.server.member.dto.UpdateRequestDto;

public interface MemberService {

	MemberInfoDto login(LoginRequestDto memberDto) throws Exception;
	void register(MemberInfoDto memberDto) throws Exception;
	MemberInfoDto userInfo(String userId) throws Exception;
	void updateUserInfo(UpdateRequestDto memberDto) throws Exception;
	void deleteUser(String userId) throws Exception;

	void saveRefreshToken(String userId, String refreshToken) throws Exception;
	Object getRefreshToken(String userId) throws Exception;
	void deleRefreshToken(String userId) throws Exception;

	boolean checkIdAvailability(String userId) throws Exception;

	String findUserId(String name, String email) throws Exception;
	void resetPassword(String name, String userId, String newPassword) throws Exception;
}
