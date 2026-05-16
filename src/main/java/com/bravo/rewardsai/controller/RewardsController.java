package com.bravo.rewardsai.controller;

import com.bravo.rewardsai.entity.Coupon;
import com.bravo.rewardsai.entity.Reward;
import com.bravo.rewardsai.service.RewardService;
import com.bravo.rewardsai.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@CrossOrigin(origins = "*")
public class RewardsController {

    @Autowired
    private RewardService rewardService;

    @GetMapping("/coupons")
    public ResponseEntity<ApiResponse<List<Coupon>>> getCoupons() {
        return ResponseEntity.ok(ApiResponse.success(rewardService.getAvailableCoupons(), "Fetched available coupons"));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Reward>>> getActiveRewards() {
        String username = getCurrentUsername();
        return ResponseEntity.ok(ApiResponse.success(rewardService.getActiveRewards(username), "Fetched active rewards"));
    }

    @PostMapping("/redeem/{couponId}")
    public ResponseEntity<ApiResponse<Reward>> redeemCoupon(@PathVariable Long couponId) {
        String username = getCurrentUsername();
        try {
            Reward reward = rewardService.redeemCoupon(username, couponId);
            return ResponseEntity.ok(ApiResponse.success(reward, "Coupon redeemed successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
