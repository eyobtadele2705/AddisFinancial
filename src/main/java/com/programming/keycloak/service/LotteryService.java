package com.programming.keycloak.service;

import com.programming.keycloak.commons.Coupon;
import com.programming.keycloak.dto.LotteryResponse;
import com.programming.keycloak.model.records.NewBet;

import java.util.List;

public interface LotteryService {

    LotteryResponse placeBet(NewBet newBet);

    List<Coupon> availableBets();
}
