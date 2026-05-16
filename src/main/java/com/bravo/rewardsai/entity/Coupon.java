package com.bravo.rewardsai.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String discountValue; // e.g. "-20%", "5 AZN"
    private long coinCost;
    private String imageUrl;

    public Coupon() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDiscountValue() { return discountValue; }
    public void setDiscountValue(String discountValue) { this.discountValue = discountValue; }
    public long getCoinCost() { return coinCost; }
    public void setCoinCost(long coinCost) { this.coinCost = coinCost; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public static CouponBuilder builder() { return new CouponBuilder(); }

    public static class CouponBuilder {
        private Coupon coupon = new Coupon();
        public CouponBuilder title(String title) { coupon.setTitle(title); return this; }
        public CouponBuilder description(String description) { coupon.setDescription(description); return this; }
        public CouponBuilder discountValue(String discountValue) { coupon.setDiscountValue(discountValue); return this; }
        public CouponBuilder coinCost(long coinCost) { coupon.setCoinCost(coinCost); return this; }
        public CouponBuilder imageUrl(String imageUrl) { coupon.setImageUrl(imageUrl); return this; }
        public Coupon build() { return coupon; }
    }
}
