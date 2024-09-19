package com.programming.keycloak.api;

import com.programming.keycloak.commons.Coupon;
import com.programming.keycloak.dto.LotteryResponse;
import com.programming.keycloak.model.records.NewBet;
import com.programming.keycloak.service.LotteryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lottery")
@RequiredArgsConstructor
public class LotteryController {

    private final LotteryService lotteryService;

    @PostMapping("/bets/place-bet")
    public ResponseEntity<LotteryResponse> placeBet(
            @RequestBody NewBet newBet,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(lotteryService.placeBet(newBet));

    }

    @GetMapping("/bets/get-avalilable-bets")
    public ResponseEntity<List<Coupon>> getAvailableBets(){
        return ResponseEntity.ok(lotteryService.availableBets());
    }
}
