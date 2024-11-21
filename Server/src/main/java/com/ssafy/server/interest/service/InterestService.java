package com.ssafy.server.interest.service;

import com.ssafy.server.interest.dto.InterestAptDto;
import java.util.List;

public interface InterestService {

    void addInterest(String userId, String aptSeq) throws Exception;
    void removeInterest(String userId, String aptSeq) throws Exception;
    List<InterestAptDto> getInterestApts(String userId) throws Exception;
    boolean checkInterestStatus(String userId, String aptSeq) throws Exception;
}
