package com.ssafy.server.chatbot.dto;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationAnalysisRequest {
    private String address;
    private Map<String, List<FacilityDetail>> facilityDetails;
}
