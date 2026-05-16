package com.bravo.rewardsai.service;

import com.bravo.rewardsai.entity.Coupon;
import com.bravo.rewardsai.entity.Reward;
import com.bravo.rewardsai.entity.User;
import com.bravo.rewardsai.repository.CouponRepository;
import com.bravo.rewardsai.repository.RewardRepository;
import com.bravo.rewardsai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RewardService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Coupon> getAvailableCoupons() {
        return couponRepository.findAll();
    }

    public List<Reward> getActiveRewards(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return rewardRepository.findByUserAndIsUsedFalse(user);
    }

    @Transactional
    public Reward redeemCoupon(String username, Long couponId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        if (user.getCoinBalance() < coupon.getCoinCost()) {
            throw new RuntimeException("Insufficient coin balance");
        }

        user.setCoinBalance(user.getCoinBalance() - coupon.getCoinCost());
        userRepository.save(user);

        Reward reward = Reward.builder()
                .user(user)
                .coupon(coupon)
                .expiryDate(LocalDateTime.now().plusDays(30))
                .isUsed(false)
                .build();

        return rewardRepository.save(reward);
    }
}
