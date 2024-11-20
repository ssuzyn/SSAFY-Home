package com.ssafy.server.interest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestAptDto {
    private String aptSeq;
    private String aptName;
    private String latestDealAmount;
    private String prevDealAmount;
    private double priceChangeRate;
}
