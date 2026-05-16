package com.bravo.rewardsai.controller;

import com.bravo.rewardsai.entity.Product;
import com.bravo.rewardsai.service.ProductService;
import com.bravo.rewardsai.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/recommended")
    public ResponseEntity<ApiResponse<List<Product>>> getRecommended() {
        return ResponseEntity.ok(ApiResponse.success(productService.getRecommendedProducts(), "Fetched recommended products"));
    }

    @GetMapping("/offers")
    public ResponseEntity<ApiResponse<List<Product>>> getOffers() {
        return ResponseEntity.ok(ApiResponse.success(productService.getPersonalizedOffers(), "Fetched personalized offers"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(productService.getAllProducts(), "Fetched all products"));
    }
}
