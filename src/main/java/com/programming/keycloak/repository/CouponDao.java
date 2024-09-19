package com.programming.keycloak.repository;

import com.programming.keycloak.commons.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponDao extends JpaRepository<Coupon, Long> {

    @Query(value = """
            SELECT * 
            FROM coupon c
            WHERE c.available_coupons > 0 
            """, nativeQuery = true)

    List<Coupon> getAvailableCoupons();

    @Query(value = """
            SELECT *
            FROM coupon c
            WHERE c.coupon_number = :couponNumber
    """, nativeQuery = true)
    Optional<Coupon> findCouponByCouponNumber(Integer couponNumber);
}
