package com.ssafy.server.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDetail {
    private String name;
    private Double distance;  // 매물로부터의 거리 (미터)
    private String category;
}
