package com.bravo.rewardsai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private LocalDateTime expiryDate;
    private boolean isUsed = false;
    private LocalDateTime usedAt;

    public Reward() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Coupon getCoupon() { return coupon; }
    public void setCoupon(Coupon coupon) { this.coupon = coupon; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }
    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }

    public static RewardBuilder builder() { return new RewardBuilder(); }

    public static class RewardBuilder {
        private Reward reward = new Reward();
        public RewardBuilder user(User user) { reward.setUser(user); return this; }
        public RewardBuilder coupon(Coupon coupon) { reward.setCoupon(coupon); return this; }
        public RewardBuilder expiryDate(LocalDateTime expiryDate) { reward.setExpiryDate(expiryDate); return this; }
        public RewardBuilder isUsed(boolean isUsed) { reward.setUsed(isUsed); return this; }
        public Reward build() { return reward; }
    }
}
