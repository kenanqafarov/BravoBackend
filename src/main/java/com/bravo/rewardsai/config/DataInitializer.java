package com.bravo.rewardsai.config;

import com.bravo.rewardsai.entity.Coupon;
import com.bravo.rewardsai.entity.Product;
import com.bravo.rewardsai.entity.User;
import com.bravo.rewardsai.repository.CouponRepository;
import com.bravo.rewardsai.repository.ProductRepository;
import com.bravo.rewardsai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            seedUsers();
        }
        if (productRepository.count() == 0) {
            seedProducts();
        }
        if (couponRepository.count() == 0) {
            seedCoupons();
        }
    }

    private void seedUsers() {
        User admin = User.builder()
                .username("admin")
                .email("admin@bravo.az")
                .password(passwordEncoder.encode("admin123"))
                .fullName("Admin User")
                .coinBalance(5000)
                .userLevel("Platinum")
                .roles(new HashSet<>(Set.of(User.Role.ROLE_ADMIN, User.Role.ROLE_USER)))
                .build();

        User user = User.builder()
                .username("lale")
                .email("lale@bravo.az")
                .password(passwordEncoder.encode("password123"))
                .fullName("Lale Memmedova")
                .coinBalance(1240)
                .userLevel("Gold")
                .roles(new HashSet<>(Set.of(User.Role.ROLE_USER)))
                .build();

        userRepository.saveAll(List.of(admin, user));
    }

    private void seedProducts() {
        List<Product> products = List.of(
            Product.builder()
                .name("Belgian Chocolate Bar")
                .price(42.90)
                .label("-15%")
                .category("Sweets")
                .imageUrl("https://images.unsplash.com/photo-1549007994-cb92caebd54b?w=300")
                .isPersonalizedOffer(true)
                .build(),
            Product.builder()
                .name("Premium Coffee Beans")
                .price(89.50)
                .label("-15%")
                .category("Beverages")
                .imageUrl("https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=300")
                .isPersonalizedOffer(true)
                .build(),
            Product.builder()
                .name("Cold Press Juice")
                .price(28.00)
                .category("Health")
                .imageUrl("https://images.unsplash.com/photo-1622597467827-43f0636bc9ce?w=200")
                .isRecommended(true)
                .emoji("🧃")
                .build(),
            Product.builder()
                .name("Organic Pasta")
                .price(19.90)
                .category("Grains")
                .imageUrl("https://images.unsplash.com/photo-1551462147-37885abb3e92?w=200")
                .isRecommended(true)
                .emoji("🍝")
                .build()
        );
        productRepository.saveAll(products);
    }

    private void seedCoupons() {
        List<Coupon> coupons = List.of(
            Coupon.builder()
                .title("-20% on Dairy Products")
                .description("Valid for your next purchase")
                .coinCost(200)
                .discountValue("-20%")
                .imageUrl("https://images.unsplash.com/photo-1550583724-125581f77833?w=200")
                .build(),
            Coupon.builder()
                .title("-15% on Fresh Fruits")
                .description("Valid for your next purchase")
                .coinCost(150)
                .discountValue("-15%")
                .imageUrl("https://images.unsplash.com/photo-1610832958506-aa56368176cf?w=200")
                .build()
        );
        couponRepository.saveAll(coupons);
    }
}
