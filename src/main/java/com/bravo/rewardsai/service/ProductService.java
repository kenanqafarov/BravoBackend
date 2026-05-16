package com.bravo.rewardsai.service;

import com.bravo.rewardsai.entity.Product;
import com.bravo.rewardsai.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getRecommendedProducts() {
        return productRepository.findByIsRecommendedTrue();
    }

    public List<Product> getPersonalizedOffers() {
        return productRepository.findByIsPersonalizedOfferTrue();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
