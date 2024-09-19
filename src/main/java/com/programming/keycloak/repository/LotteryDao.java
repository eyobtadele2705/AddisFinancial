package com.programming.keycloak.repository;

import com.programming.keycloak.model.Bets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotteryDao extends JpaRepository<Bets, Long> {
}
