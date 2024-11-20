package com.ssafy.server.interest.service;

import com.ssafy.server.interest.dto.InterestAptDto;
import com.ssafy.server.interest.mapper.InterestMapper;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {
    private final InterestMapper interestMapper;

    @Transactional
    public void addInterest(String userId, String aptSeq) throws SQLException {
        // 중복 체크
        if (interestMapper.existsInterest(userId, aptSeq)) {
            throw new IllegalStateException("이미 관심 매물로 등록되어 있습니다.");
        }
        interestMapper.insertInterest(userId, aptSeq);
    }

    @Transactional
    public void removeInterest(String userId, String aptSeq) throws SQLException {
        if (interestMapper.deleteInterest(userId, aptSeq) == 0) {
            throw new IllegalStateException("관심 매물을 찾을 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<InterestAptDto> getInterestApts(String userId) throws SQLException {
        return interestMapper.getInterestApts(userId);
    }

    @Transactional(readOnly = true)
    public boolean checkInterestStatus(String userId, String aptSeq) throws SQLException {
        return interestMapper.existsInterest(userId, aptSeq);
    }
}
