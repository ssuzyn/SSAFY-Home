package com.ssafy.server.interest.mapper;

import com.ssafy.server.interest.dto.InterestAptDto;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InterestMapper {

    void insertInterest(String userId, String aptSeq) throws SQLException;
    int deleteInterest(String userId, String aptSeq) throws SQLException;
    List<InterestAptDto> getInterestApts(String userId) throws SQLException;
    boolean existsInterest(String userId, String aptSeq) throws SQLException;

}
