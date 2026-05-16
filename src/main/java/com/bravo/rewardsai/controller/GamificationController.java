package com.bravo.rewardsai.controller;

import com.bravo.rewardsai.entity.User;
import com.bravo.rewardsai.service.GamificationService;
import com.bravo.rewardsai.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GamificationController {

    @Autowired
    private GamificationService gamificationService;

    @PostMapping("/complete")
    public ResponseEntity<ApiResponse<User>> completeSession(@RequestBody Map<String, Object> payload) {
        String username = getCurrentUsername();
        int coins = (int) payload.getOrDefault("coins", 0);
        
        User user = gamificationService.addCoins(username, coins);
        return ResponseEntity.ok(ApiResponse.success(user, "Session completed and coins added!"));
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
