package com.ssafy.server.house.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DealInfoDto {
	private String floor;
    private double excluUseAr;
    private String dealAmount;
    private String dealDate;
}
