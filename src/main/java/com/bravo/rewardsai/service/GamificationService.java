package com.bravo.rewardsai.service;

import com.bravo.rewardsai.entity.User;
import com.bravo.rewardsai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GamificationService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User addCoins(String username, int coins) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        user.setCoinBalance(user.getCoinBalance() + coins);
        // Update experience points as well
        user.setExperiencePoints(user.getExperiencePoints() + (coins / 2));
        
        return userRepository.save(user);
    }
}
