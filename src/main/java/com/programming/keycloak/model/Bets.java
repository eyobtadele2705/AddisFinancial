package com.programming.keycloak.model;

import com.programming.keycloak.enums.LotteryOption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "_bets")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Bets {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bets_seq")
    @SequenceGenerator(name = "bets_seq", sequenceName = "_bets_seq", allocationSize = 1)
    private Long id;
    private String betDescription;
    private double betAmount;
    private String betType;
    private Integer couponNumber;
    private int totalCoupon;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
