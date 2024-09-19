package com.programming.keycloak.service.impl;

import com.programming.keycloak.commons.Coupon;
import com.programming.keycloak.dto.LotteryResponse;
import com.programming.keycloak.model.Bets;
import com.programming.keycloak.model.User;
import com.programming.keycloak.model.records.NewBet;
import com.programming.keycloak.repository.CouponDao;
import com.programming.keycloak.repository.LotteryDao;
import com.programming.keycloak.service.LotteryService;
import com.programming.keycloak.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LotteryServiceImpl implements LotteryService {

    private final LotteryDao lotteryDao;
    private final UserService userService;
    private final CouponDao couponDao;

    @Override
    public LotteryResponse placeBet(NewBet newBet) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaim("sub");

        User user = userService.findUserById(userId);

        Coupon coupon = couponDao.findCouponByCouponNumber(newBet.couponNumber())
                .orElseThrow(() ->new RuntimeException("No coupon found"));

        if (coupon.getAvailableCoupons() < newBet.totalCoupon()){
            throw new RuntimeException("Not enough available coupons");
        }
        try {
            Bets bets = Bets.builder()
                    .betType(newBet.betType())
                    .betAmount(newBet.betAmount())
                    .betDescription(newBet.betDescription())
                    .couponNumber(newBet.couponNumber())
                    .totalCoupon(newBet.totalCoupon())
                    .user(user)
                    .build();

            Bets savedBet = lotteryDao.save(bets);
            coupon.setAvailableCoupons(coupon.getAvailableCoupons() - newBet.totalCoupon());
            couponDao.save(coupon);

            return LotteryResponse.builder()
                    .bettingId(savedBet.getId())
                    .coupon(
                            Coupon.builder()
                                    .couponNumber(savedBet.getCouponNumber())
                            .build()
                    )
                    .totalCoupon(savedBet.getTotalCoupon())
                    .build();
        }
        catch (Exception e) {
            log.error("Error placing bet: {}", e.getMessage());
            throw new RuntimeException("Error putting bet: {}" + e.getMessage());
        }
    }

    @Override
    public List<Coupon> availableBets() {
        return couponDao.getAvailableCoupons();

    }
}
