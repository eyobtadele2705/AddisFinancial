package com.programming.keycloak.model.records;


public record NewBet(
        String betDescription,
        double betAmount,
        String betType,
        Integer couponNumber,
        int totalCoupon
) {
}
