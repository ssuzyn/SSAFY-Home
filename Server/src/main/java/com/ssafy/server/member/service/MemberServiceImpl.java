package com.ssafy.server.member.service;

import com.ssafy.server.member.dto.FileInfoDto;
import com.ssafy.server.member.dto.UpdateRequestDto;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ssafy.server.member.dto.LoginRequestDto;
import com.ssafy.server.member.dto.MemberInfoDto;
import com.ssafy.server.member.mapper.MemberMapper;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberMapper memberMapper;
	private final FileService fileService;

	@Override
	public MemberInfoDto login(LoginRequestDto memberDto) throws Exception {
		return memberMapper.login(memberDto);
	}

	@Override
	public void register(MemberInfoDto memberDto) throws Exception {
		memberMapper.register(memberDto);  // 회원가입 처리
	}
	
	@Override
	public MemberInfoDto userInfo(String userId) throws Exception {
		MemberInfoDto memberDto = memberMapper.findByUserId(userId);
		FileInfoDto fileInfoDto = fileService.getProfileImage(userId);
		memberDto.setProfileSavePath(fileInfoDto.getSavePath());
		return memberDto;
	}
	
	@Override
	public void updateUserInfo(UpdateRequestDto memberDto) throws Exception {
		memberMapper.updateUserInfo(memberDto);
	}

	public void deleteUser(String userId) throws Exception {
		// 1. 회원의 프로필 파일 삭제
		fileService.deleteFilesByUserId(userId);

		// 2. 회원 삭제
		memberMapper.deleteByUserId(userId);
	}

	@Override
	public void saveRefreshToken(String userId, String refreshToken) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("token", refreshToken);
		memberMapper.saveRefreshToken(map);
	}

	@Override
	public Object getRefreshToken(String userId) throws Exception {
		return memberMapper.getRefreshToken(userId);
	}

	@Override
	public void deleRefreshToken(String userId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("token", null);
		memberMapper.deleteRefreshToken(map);
	}

	@Override
	public boolean checkIdAvailability(String userId) throws Exception {
		int count = memberMapper.checkIdAvailability(userId);
		return count == 0;
	}
}
