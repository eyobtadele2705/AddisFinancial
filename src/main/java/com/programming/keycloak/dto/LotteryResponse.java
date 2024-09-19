package com.programming.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.programming.keycloak.commons.Coupon;
import com.programming.keycloak.enums.LotteryOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(NON_NULL)
public class LotteryResponse {

    private Long bettingId;
    private Coupon coupon;
    private int totalCoupon;
    private String lotteryType;

}
