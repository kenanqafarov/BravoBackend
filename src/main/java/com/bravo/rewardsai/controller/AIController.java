package com.bravo.rewardsai.controller;

import com.bravo.rewardsai.entity.Product;
import com.bravo.rewardsai.repository.ProductRepository;
import com.bravo.rewardsai.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<Map<String, String>>> chat(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");
        Map<String, String> response = new HashMap<>();
        
        // Mock AI response logic
        if (message.toLowerCase().contains("healthy")) {
            response.put("reply", "I've found some organic pasta and fresh juices that match your healthy criteria! Should I add them to your basket?");
        } else {
            response.put("reply", "Great choice! I'm analyzing your preferences and our current stock to build the perfect basket for you. ✨");
        }
        
        return ResponseEntity.ok(ApiResponse.success(response, "AI Response fetched"));
    }

    @GetMapping("/generate-basket")
    public ResponseEntity<ApiResponse<List<Product>>> generateBasket() {
        // Return recommended products as a generated basket
        List<Product> basketItems = productRepository.findByIsRecommendedTrue();
        return ResponseEntity.ok(ApiResponse.success(basketItems, "Healthy basket generated successfully!"));
    }
}
