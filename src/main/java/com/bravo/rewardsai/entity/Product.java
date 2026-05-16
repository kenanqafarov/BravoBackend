package com.bravo.rewardsai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private String currency = "AZN";
    private String category;
    private String imageUrl;
    private String emoji;
    private String label; // e.g. "NEW", "-15%", "RECOMMENDED"

    private boolean isRecommended = false;
    private boolean isPersonalizedOffer = false;

    public Product() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public boolean isRecommended() { return isRecommended; }
    public void setRecommended(boolean recommended) { isRecommended = recommended; }
    public boolean isPersonalizedOffer() { return isPersonalizedOffer; }
    public void setPersonalizedOffer(boolean personalizedOffer) { isPersonalizedOffer = personalizedOffer; }

    public static ProductBuilder builder() { return new ProductBuilder(); }

    public static class ProductBuilder {
        private Product product = new Product();
        public ProductBuilder name(String name) { product.setName(name); return this; }
        public ProductBuilder description(String description) { product.setDescription(description); return this; }
        public ProductBuilder price(double price) { product.setPrice(price); return this; }
        public ProductBuilder category(String category) { product.setCategory(category); return this; }
        public ProductBuilder imageUrl(String imageUrl) { product.setImageUrl(imageUrl); return this; }
        public ProductBuilder emoji(String emoji) { product.setEmoji(emoji); return this; }
        public ProductBuilder label(String label) { product.setLabel(label); return this; }
        public ProductBuilder isRecommended(boolean isRecommended) { product.setRecommended(isRecommended); return this; }
        public ProductBuilder isPersonalizedOffer(boolean isPersonalizedOffer) { product.setPersonalizedOffer(isPersonalizedOffer); return this; }
        public Product build() { return product; }
    }
}
