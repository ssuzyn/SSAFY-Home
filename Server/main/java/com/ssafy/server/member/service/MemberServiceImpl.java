package com.ssafy.server.member.service;

import com.ssafy.server.member.dto.MemberDto;
import com.ssafy.server.member.mapper.MemberMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
	
	private final MemberMapper memberMapper;

	public MemberServiceImpl(MemberMapper memberMapper) {
		super();
		this.memberMapper = memberMapper;
	}

	@Override
	public MemberDto login(MemberDto memberDto) throws Exception {
		return memberMapper.login(memberDto);
	}

	@Override
	public void register(MemberDto memberDto) throws Exception {
		memberMapper.register(memberDto);  // 회원가입 처리
	}
	
	@Override
	public MemberDto userInfo(String userId) throws Exception {
		return memberMapper.userInfo(userId);
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

}
